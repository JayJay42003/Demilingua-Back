package com.demilingua.backend.services;

import com.demilingua.backend.entities.Division;
import com.demilingua.backend.entities.LigaSemanal;
import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.repositories.DivisionRepository;
import com.demilingua.backend.repositories.LigaSemanalRepository;
import com.demilingua.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
public class LigaSchedulerService {

    @Autowired
    private LigaSemanalRepository ligaSemanalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    /**
     * Se ejecuta todos los domingos a las 23:59:59 (Cron: sec min hour day month dayOfWeek)
     * Procesa ascensos y descensos basados en el XP de la semana.
     */
    @Scheduled(cron = "59 59 23 * * SUN")
    @Transactional
    public void procesarFinDeSemana() {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Date dateStart = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<LigaSemanal> rankingSemanal = ligaSemanalRepository.findByFechaInicioSemanaOrderByXpSemanalDesc(dateStart);

        if (rankingSemanal.isEmpty()) return;

        int totalParticipantes = rankingSemanal.size();
        // Top 20% asciende, Bottom 20% desciende (mínimo 1 si hay suficientes personas)
        int ascensos = Math.max(1, (int) (totalParticipantes * 0.2));
        int descensos = Math.max(1, (int) (totalParticipantes * 0.2));

        for (int i = 0; i < totalParticipantes; i++) {
            LigaSemanal registro = rankingSemanal.get(i);
            Usuario usuario = registro.getUsuario();
            Division divisionActual = usuario.getDivision();

            if (divisionActual == null) continue;

            if (i < ascensos) {
                // Ascender
                divisionRepository.findById(divisionActual.getId() + 1).ifPresent(usuario::setDivision);
            } else if (i >= (totalParticipantes - descensos) && divisionActual.getId() > 1) {
                // Descender (pero no por debajo de la división 1)
                divisionRepository.findById(divisionActual.getId() - 1).ifPresent(usuario::setDivision);
            }
            
            usuarioRepository.save(usuario);
        }
        
        System.out.println("Ligas procesadas exitosamente para la semana del " + startOfWeek);
    }
}

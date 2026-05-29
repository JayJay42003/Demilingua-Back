package com.demilingua.backend.services;

import com.demilingua.backend.entities.Idioma;
import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.entities.UsuarioIdioma;
import com.demilingua.backend.entities.UsuarioIdiomaId;
import com.demilingua.backend.entities.LigaSemanal;
import com.demilingua.backend.repositories.IdiomaRepository;
import com.demilingua.backend.repositories.UsuarioIdiomaRepository;
import com.demilingua.backend.repositories.UsuarioRepository;
import com.demilingua.backend.repositories.LigaSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
public class GamificationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IdiomaRepository idiomaRepository;

    @Autowired
    private UsuarioIdiomaRepository usuarioIdiomaRepository;

    @Autowired
    private LigaSemanalRepository ligaSemanalRepository;

    @Transactional
    public Map<String, String> getStatus(int usuarioId) {
        Map<String, String> res = new HashMap<>();

        Optional<Usuario> optionalUser = usuarioRepository.findById(usuarioId);
        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();

            // 1. Lógica de recarga de vidas
            LocalDate today = LocalDate.now();
            LocalDate lastRecharge = usuario.getUltimaRecarga().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (lastRecharge.isBefore(today)) {
                usuario.setVidas(5);
                usuario.setUltimaRecarga(new Date());
                usuarioRepository.save(usuario);
            }

            // 2. Lógica de pérdida de racha (Si no ha practicado ayer ni hoy, racha = 0)
            if (usuario.getFechaUltimaLeccion() != null) {
                LocalDate lastLesson = usuario.getFechaUltimaLeccion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long daysSinceLastLesson = ChronoUnit.DAYS.between(lastLesson, today);
                if (daysSinceLastLesson > 1) {
                    usuario.setRachaActual(0);
                    usuarioRepository.save(usuario);
                }
            }

            res.put("vidas", String.valueOf(usuario.getVidas()));
            res.put("racha", String.valueOf(usuario.getRachaActual()));
            res.put("division_id", usuario.getDivision() != null ? String.valueOf(usuario.getDivision().getId()) : "null");
        } else {
            res.put("status", "error");
            res.put("message", "Usuario no encontrado");
        }
        return res;
    }

    @Transactional
    public Map<String, String> perderVida(int usuarioId) {
        Map<String, String> res = new HashMap<>();

        Optional<Usuario> optionalUser = usuarioRepository.findById(usuarioId);
        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();
            if (usuario.getVidas() > 0) {
                usuario.setVidas(usuario.getVidas() - 1);
                usuarioRepository.save(usuario);
                res.put("status", "ok");
                res.put("vidas_restantes", String.valueOf(usuario.getVidas()));
            } else {
                res.put("status", "sin_vidas");
            }
        } else {
            res.put("status", "error");
        }
        return res;
    }

    @Transactional
    public Map<String, String> addXp(int usuarioId, int idiomaId, int puntos) {
        Map<String, String> res = new HashMap<>();

        try {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
            Idioma idioma = idiomaRepository.findById(idiomaId).orElseThrow(() -> new NoSuchElementException("Idioma no encontrado"));

            // 1. Sumar a UsuarioIdioma (Existente)
            UsuarioIdiomaId id = new UsuarioIdiomaId(usuarioId, idiomaId);
            UsuarioIdioma usuarioIdioma = usuarioIdiomaRepository.findById(id).orElse(new UsuarioIdioma());

            if (usuarioIdioma.getId() == null) {
                usuarioIdioma.setId(id);
                usuarioIdioma.setUsuario(usuario);
                usuarioIdioma.setIdioma(idioma);
                usuarioIdioma.setPuntos(puntos);
            } else {
                usuarioIdioma.setPuntos(usuarioIdioma.getPuntos() + puntos);
            }
            usuarioIdiomaRepository.save(usuarioIdioma);

            // 2. Sumar a Liga Semanal
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            Date dateStart = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            LigaSemanal liga = ligaSemanalRepository.findByUsuarioAndFechaInicioSemana(usuario, dateStart)
                    .orElse(new LigaSemanal());
            
            if (liga.getId() == null) {
                liga.setUsuario(usuario);
                liga.setFechaInicioSemana(dateStart);
                liga.setXpSemanal(puntos);
            } else {
                liga.setXpSemanal(liga.getXpSemanal() + puntos);
            }
            ligaSemanalRepository.save(liga);

            // 3. Lógica de Racha (Aumentar racha si es el primer test del día)
            if (usuario.getFechaUltimaLeccion() == null) {
                usuario.setRachaActual(1);
            } else {
                LocalDate lastLesson = usuario.getFechaUltimaLeccion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (lastLesson.isBefore(today)) {
                    usuario.setRachaActual(usuario.getRachaActual() + 1);
                }
            }
            usuario.setFechaUltimaLeccion(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            usuarioRepository.save(usuario);

            res.put("status", "ok");
            res.put("racha_actual", String.valueOf(usuario.getRachaActual()));
        } catch (Exception e) {
            res.put("status", "error");
        }
        
        return res;
    }
}
package com.demilingua.backend.services;

import com.demilingua.backend.entities.Idioma;
import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.entities.UsuarioIdioma;
import com.demilingua.backend.entities.UsuarioIdiomaId;
import com.demilingua.backend.repositories.IdiomaRepository;
import com.demilingua.backend.repositories.UsuarioIdiomaRepository;
import com.demilingua.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GamificationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IdiomaRepository idiomaRepository;

    @Autowired
    private UsuarioIdiomaRepository usuarioIdiomaRepository;

    @Transactional
    public Map<String, String> getStatus(int usuarioId) {
        Map<String, String> res = new HashMap<>();

        Optional<Usuario> optionalUser = usuarioRepository.findById(usuarioId);
        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();

            // Lógica de recarga de vidas (equivalente a DATE(ultima_recarga) < CURDATE())
            LocalDate today = LocalDate.now();
            LocalDate lastRecharge = usuario.getUltimaRecarga().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (lastRecharge.isBefore(today)) {
                usuario.setVidas(5);
                usuario.setUltimaRecarga(new Date());
                usuarioRepository.save(usuario);
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
            } else {
                res.put("status", "sin_vidas");
            }
        } else {
            res.put("status", "error");
        }
        return res;
    }

    @Autowired
    private LigaSemanalRepository ligaSemanalRepository;

    @Transactional
    public Map<String, String> addXp(int usuarioId, int idiomaId, int puntos) {
        Map<String, String> res = new HashMap<>();

        try {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
            Idioma idioma = idiomaRepository.findById(idiomaId).orElseThrow();

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

            // 2. Sumar a Liga Semanal (Nuevo para Ligas)
            java.time.LocalDate startOfWeek = java.time.LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            java.util.Date dateStart = java.util.Date.from(startOfWeek.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            
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

            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        
        return res;
    }
}
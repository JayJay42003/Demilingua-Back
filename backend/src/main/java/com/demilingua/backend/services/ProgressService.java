package com.demilingua.backend.services;

import com.demilingua.backend.entities.UsuarioTest;
import com.demilingua.backend.entities.UsuarioTestId;
import com.demilingua.backend.entities.UsuarioIdioma;
import com.demilingua.backend.repositories.UsuarioTestRepository;
import com.demilingua.backend.repositories.UsuarioIdiomaRepository;
import com.demilingua.backend.repositories.UsuarioRepository;
import com.demilingua.backend.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    @Autowired
    private UsuarioTestRepository usuarioTestRepository;

    @Autowired
    private UsuarioIdiomaRepository usuarioIdiomaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TestRepository testRepository;

    public List<Map<String, String>> getProgress(int usuarioId) {
        List<UsuarioIdioma> uiList = usuarioIdiomaRepository.findByUsuarioId(usuarioId);
        return uiList.stream().map(ui -> {
            Map<String, String> row = new HashMap<>();
            row.put("idioma_id", String.valueOf(ui.getIdioma().getId()));
            row.put("puntos", String.valueOf(ui.getPuntos()));
            return row;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, String> saveTestProgress(int usuarioId, int testId, int puntuacion) {
        Map<String, String> res = new HashMap<>();
        try {
            UsuarioTestId id = new UsuarioTestId(usuarioId, testId);
            UsuarioTest ut = usuarioTestRepository.findById(id).orElse(new UsuarioTest());
            
            if (ut.getId() == null) {
                ut.setId(id);
                ut.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow());
                ut.setTest(testRepository.findById(testId).orElseThrow());
                ut.setPuntuacion(puntuacion);
            } else {
                ut.setPuntuacion(Math.max(ut.getPuntuacion(), puntuacion));
            }
            
            usuarioTestRepository.save(ut);
            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }

    @Transactional
    public Map<String, String> deleteProgress(int usuarioId, int idiomaId) {
        Map<String, String> res = new HashMap<>();
        try {
            usuarioIdiomaRepository.deleteById(new com.demilingua.backend.entities.UsuarioIdiomaId(usuarioId, idiomaId));
            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }
}
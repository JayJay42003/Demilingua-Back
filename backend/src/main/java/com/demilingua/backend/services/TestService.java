package com.demilingua.backend.services;

import com.demilingua.backend.entities.Test;
import com.demilingua.backend.entities.Ejercicio;
import com.demilingua.backend.entities.ObjetoEjercicio;
import com.demilingua.backend.repositories.TestRepository;
import com.demilingua.backend.repositories.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    public List<Map<String, Object>> getTestsByCurso(int cursoId) {
        List<Test> tests = testRepository.findByCursoId(cursoId);
        return tests.stream().map(t -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("curso_id", t.getCurso().getId());
            map.put("titulo", t.getTitulo());
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getFullTest(int testId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Ejercicio> ejercicios = ejercicioRepository.findByTestId(testId);

        for (Ejercicio e : ejercicios) {
            Map<String, Object> eMap = new HashMap<>();
            eMap.put("id", e.getId());
            eMap.put("tipo", e.getTipo());
            eMap.put("puntuacion", e.getPuntuacion());

            List<Map<String, Object>> objetosList = new ArrayList<>();
            for (ObjetoEjercicio obj : e.getObjetos()) {
                Map<String, Object> oMap = new HashMap<>();
                oMap.put("contenido", obj.getContenido());
                oMap.put("respuesta_correcta", obj.getRespuestaCorrecta());
                oMap.put("opciones", obj.getOpciones());
                objetosList.add(oMap);
            }
            eMap.put("objetos", objetosList);
            result.add(eMap);
        }
        return result;
    }
}
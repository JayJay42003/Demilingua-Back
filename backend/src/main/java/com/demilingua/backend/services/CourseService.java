package com.demilingua.backend.services;

import com.demilingua.backend.entities.Curso;
import com.demilingua.backend.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Map<String, Object>> getCursosByIdioma(int idiomaId) {
        List<Curso> cursos = cursoRepository.findByIdiomaId(idiomaId);
        return cursos.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("idioma_id", c.getIdioma().getId());
            map.put("nombre", c.getNombre());
            map.put("descripcion", c.getDescripcion());
            map.put("dificultad", c.getDificultad());
            return map;
        }).collect(Collectors.toList());
    }
}
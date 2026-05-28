package com.demilingua.backend.controllers;

import com.demilingua.backend.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{idiomaId}")
    public ResponseEntity<List<Map<String, Object>>> getCursos(@PathVariable int idiomaId) {
        return ResponseEntity.ok(courseService.getCursosByIdioma(idiomaId));
    }
}
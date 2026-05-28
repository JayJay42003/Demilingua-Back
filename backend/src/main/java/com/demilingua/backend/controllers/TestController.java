package com.demilingua.backend.controllers;

import com.demilingua.backend.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/{cursoId}")
    public ResponseEntity<List<Map<String, Object>>> getTests(@PathVariable int cursoId) {
        return ResponseEntity.ok(testService.getTestsByCurso(cursoId));
    }

    @GetMapping("/ejercicios/{testId}")
    public ResponseEntity<List<Map<String, Object>>> getEjercicios(@PathVariable int testId) {
        return ResponseEntity.ok(testService.getFullTest(testId));
    }
}
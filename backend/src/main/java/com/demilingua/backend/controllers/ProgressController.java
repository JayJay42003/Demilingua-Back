package com.demilingua.backend.controllers;

import com.demilingua.backend.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Map<String, String>>> getProgress(@PathVariable int usuarioId) {
        return ResponseEntity.ok(progressService.getProgress(usuarioId));
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> saveTestProgress(@RequestParam("usuarioId") int usuarioId, @RequestParam("testId") int testId, @RequestParam("puntuacion") int puntuacion) {
        return ResponseEntity.ok(progressService.saveTestProgress(usuarioId, testId, puntuacion));
    }

    @DeleteMapping("/{usuarioId}/{idiomaId}")
    public ResponseEntity<Map<String, String>> deleteProgress(@PathVariable int usuarioId, @PathVariable int idiomaId) {
        return ResponseEntity.ok(progressService.deleteProgress(usuarioId, idiomaId));
    }
}

package com.demilingua.backend.controllers;

import com.demilingua.backend.entities.Idioma;
import com.demilingua.backend.services.IdiomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/idiomas")
public class IdiomaController {

    @Autowired
    private IdiomaService idiomaService;

    @GetMapping
    public ResponseEntity<List<Idioma>> getIdiomas() {
        return ResponseEntity.ok(idiomaService.getAllIdiomas());
    }
}

package com.demilingua.backend.controllers;

import com.demilingua.backend.services.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gamification")
public class GamificationController {

    private final GamificationService gamificationService;

    @Autowired
    public GamificationController(GamificationService gamificationService) {
        this.gamificationService = gamificationService;
    }

    @GetMapping("/status/{usuarioId}")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable int usuarioId) {
        return ResponseEntity.ok(gamificationService.getStatus(usuarioId));
    }

    @PostMapping("/perder-vida")
    public ResponseEntity<Map<String, String>> perderVida(@RequestParam("usuarioId") int usuarioId) {
        return ResponseEntity.ok(gamificationService.perderVida(usuarioId));
    }

    @PostMapping("/add-xp")
    public ResponseEntity<Map<String, String>> addXp(@RequestParam("usuarioId") int uId, @RequestParam("idiomaId") int iId, @RequestParam("puntos") int pts) {
        return ResponseEntity.ok(gamificationService.addXp(uId, iId, pts));
    }
}

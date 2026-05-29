package com.demilingua.backend.controllers;

import com.demilingua.backend.services.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getGlobalRanking() {
        return ResponseEntity.ok(rankingService.getGlobalRanking());
    }

    @GetMapping("/division/{divisionId}")
    public ResponseEntity<List<Map<String, String>>> getRankingByDivision(@PathVariable int divisionId) {
        return ResponseEntity.ok(rankingService.getRankingByDivision(divisionId));
    }
}

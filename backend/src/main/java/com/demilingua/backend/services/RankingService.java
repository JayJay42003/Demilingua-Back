package com.demilingua.backend.services;

import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Map<String, String>> getGlobalRanking() {
        List<Usuario> ranking = usuarioRepository.findTop10ByOrderByRachaActualDesc();
        return ranking.stream().map(u -> {
            Map<String, String> row = new HashMap<>();
            row.put("nombre", u.getNombre());
            row.put("racha", String.valueOf(u.getRachaActual()));
            row.put("division", u.getDivision() != null ? u.getDivision().getNombre() : "Sin División");
            return row;
        }).collect(Collectors.toList());
    }

    public List<Map<String, String>> getRankingByDivision(int divisionId) {
        List<Usuario> ranking = usuarioRepository.findByDivisionIdOrderByRachaActualDesc(divisionId);
        return ranking.stream().map(u -> {
            Map<String, String> row = new HashMap<>();
            row.put("nombre", u.getNombre());
            row.put("racha", String.valueOf(u.getRachaActual()));
            return row;
        }).collect(Collectors.toList());
    }
}

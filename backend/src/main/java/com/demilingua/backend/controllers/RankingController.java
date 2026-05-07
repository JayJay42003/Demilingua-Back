package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    // LEER Ranking Global (Top 10 por racha o puntos totales)
    @GetMapping("/global")
    public List<Map<String, String>> getGlobalRanking() {
        List<Map<String, String>> ranking = new ArrayList<>();
        String sql = "SELECT u.nombre, u.racha_actual, d.nombre as division_nombre " +
                "FROM usuario u JOIN division d ON u.division_id = d.id " +
                "ORDER BY u.racha_actual DESC LIMIT 10";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("nombre", rs.getString("nombre"));
                row.put("racha", String.valueOf(rs.getInt("racha_actual")));
                row.put("division", rs.getString("division_nombre"));
                ranking.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ranking;
    }

    // LEER Usuarios de una misma división
    @GetMapping("/division/{divisionId}")
    public List<Map<String, String>> getRankingByDivision(@PathVariable int divisionId) {
        List<Map<String, String>> ranking = new ArrayList<>();
        String sql = "SELECT nombre, racha_actual FROM usuario WHERE division_id = ? ORDER BY racha_actual DESC";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, divisionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("nombre", rs.getString("nombre"));
                row.put("racha", String.valueOf(rs.getInt("racha_actual")));
                ranking.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ranking;
    }
}
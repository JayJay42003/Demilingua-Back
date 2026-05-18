package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/gamification")
public class GamificationController {

    @GetMapping("/status/{usuarioId}")
    public Map<String, String> getStatus(@PathVariable int usuarioId) {
        Map<String, String> res = new HashMap<>();

        String sqlUpdate = "UPDATE usuario SET vidas = 5, ultima_recarga = CURRENT_TIMESTAMP " +
                "WHERE id = ? AND DATE(ultima_recarga) < CURDATE()";

        String sqlSelect = "SELECT vidas, racha_actual, division_id FROM usuario WHERE id = ?";

        try (Connection c = DBConfig.getConnection()) {
            // Paso 1
            try (PreparedStatement psUpdate = c.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, usuarioId);
                psUpdate.executeUpdate();
            }
            // Paso 2
            try (PreparedStatement psSelect = c.prepareStatement(sqlSelect)) {
                psSelect.setInt(1, usuarioId);
                ResultSet rs = psSelect.executeQuery();
                if (rs.next()) {
                    res.put("vidas", String.valueOf(rs.getInt("vidas")));
                    res.put("racha", String.valueOf(rs.getInt("racha_actual")));
                    res.put("division_id", String.valueOf(rs.getInt("division_id")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return res;
    }

    @PostMapping("/perder-vida")
    public Map<String, String> perderVida(@RequestParam("usuarioId") int usuarioId) {
        Map<String, String> res = new HashMap<>();
        String sql = "UPDATE usuario SET vidas = vidas - 1 WHERE id = ? AND vidas > 0";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            int filas = ps.executeUpdate();
            res.put("status", filas > 0 ? "ok" : "sin_vidas");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    @PostMapping("/add-xp")
    public Map<String, String> addXp(@RequestParam("usuarioId") int uId, @RequestParam("idiomaId") int iId, @RequestParam("puntos") int pts) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO usuario_idioma (usuario_id, idioma_id, puntos) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE puntos = puntos + ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uId);
            ps.setInt(2, iId);
            ps.setInt(3, pts);
            ps.setInt(4, pts);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
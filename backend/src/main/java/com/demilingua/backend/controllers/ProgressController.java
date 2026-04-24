package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/progreso")
public class ProgressController {

    @PostMapping("/completar")
    public Map<String, String> completarTest(@RequestParam("usuarioId") int usuarioId, @RequestParam("testId") int testId, @RequestParam("puntuacion") int puntuacion) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO usuario_test (usuario_id, test_id, puntuacion) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE puntuacion = GREATEST(puntuacion, VALUES(puntuacion))";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, testId);
            ps.setInt(3, puntuacion);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @GetMapping("/superados")
    public List<Integer> getTestsSuperados(@RequestParam("usuarioId") int usuarioId) {
        List<Integer> completados = new ArrayList<>();
        String sql = "SELECT test_id FROM usuario_test WHERE usuario_id = ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                completados.add(rs.getInt("test_id"));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return completados;
    }
}
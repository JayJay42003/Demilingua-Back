package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    // LEER Progreso de un usuario
    @GetMapping("/{usuarioId}")
    public List<Map<String, String>> getProgress(@PathVariable int usuarioId) {
        List<Map<String, String>> progreso = new ArrayList<>();
        String sql = "SELECT idioma_id, puntos FROM usuario_idioma WHERE usuario_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("idioma_id", String.valueOf(rs.getInt("idioma_id")));
                row.put("puntos", String.valueOf(rs.getInt("puntos")));
                progreso.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return progreso;
    }

    // CREAR / ACTUALIZAR Puntuación de Test (Upsert)
    @PostMapping("/test")
    public Map<String, String> saveTestProgress(@RequestParam("usuarioId") int usuarioId, @RequestParam("testId") int testId, @RequestParam("puntuacion") int puntuacion) {
        Map<String, String> res = new HashMap<>();
        // Inserta o actualiza si ya existe (requiere que la PK sea usuario_id, test_id)
        String sql = "INSERT INTO usuario_test (usuario_id, test_id, puntuacion) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE puntuacion = GREATEST(puntuacion, ?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, testId);
            ps.setInt(3, puntuacion);
            ps.setInt(4, puntuacion);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // BORRAR Progreso de un idioma
    @DeleteMapping("/{usuarioId}/{idiomaId}")
    public Map<String, String> deleteProgress(@PathVariable int usuarioId, @PathVariable int idiomaId) {
        Map<String, String> res = new HashMap<>();
        String sql = "DELETE FROM usuario_idioma WHERE usuario_id = ? AND idioma_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, idiomaId);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
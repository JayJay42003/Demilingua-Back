package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/gamificacion")
public class GamificationController {

    @PostMapping("/vidas/restar")
    public Map<String, String> restarVida(@RequestParam("usuarioId") int usuarioId) {
        Map<String, String> res = new HashMap<>();
        String sql = "UPDATE usuario SET vidas = GREATEST(vidas - 1, 0) WHERE id = ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @PostMapping("/rachas/actualizar")
    public Map<String, String> actualizarRacha(@RequestParam("usuarioId") int usuarioId, @RequestParam("xp") int xp) {
        Map<String, String> res = new HashMap<>();
        String sqlUsuario = "UPDATE usuario SET racha_actual = racha_actual + 1 WHERE id = ?";
        String sqlRacha = "INSERT INTO racha_diaria (usuario_id, fecha, xp_ganado) VALUES (?, CURDATE(), ?)";

        try (Connection c = DBConfig.getConnection()) {
            try(PreparedStatement ps1 = c.prepareStatement(sqlUsuario)) {
                ps1.setInt(1, usuarioId);
                ps1.executeUpdate();
            }
            try(PreparedStatement ps2 = c.prepareStatement(sqlRacha)) {
                ps2.setInt(1, usuarioId);
                ps2.setInt(2, xp);
                ps2.executeUpdate();
            }
            res.put("status", "ok");
        } catch (SQLException e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @PostMapping("/evaluar-liga")
    public Map<String, String> evaluarLiga(@RequestParam("usuarioId") int usuarioId) {
        Map<String, String> res = new HashMap<>();
        String sqlPuntos = "SELECT SUM(puntos) as total_xp FROM usuario_idioma WHERE usuario_id = ?";
        String sqlDivision = "SELECT id FROM division WHERE xp_minimo <= ? ORDER BY xp_minimo DESC LIMIT 1";
        String sqlUpdate = "UPDATE usuario SET division_id = ? WHERE id = ?";

        try (Connection c = DBConfig.getConnection()) {
            int totalXp = 0;
            try(PreparedStatement ps1 = c.prepareStatement(sqlPuntos)) {
                ps1.setInt(1, usuarioId);
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next()) totalXp = rs1.getInt("total_xp");
            }

            Integer nuevaDivisionId = null;
            try(PreparedStatement ps2 = c.prepareStatement(sqlDivision)) {
                ps2.setInt(1, totalXp);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) nuevaDivisionId = rs2.getInt("id");
            }

            if (nuevaDivisionId != null) {
                try(PreparedStatement ps3 = c.prepareStatement(sqlUpdate)) {
                    ps3.setInt(1, nuevaDivisionId);
                    ps3.setInt(2, usuarioId);
                    ps3.executeUpdate();
                }
            }
            res.put("status", "ok");
            res.put("division_asignada", String.valueOf(nuevaDivisionId));
        } catch (SQLException e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }
}
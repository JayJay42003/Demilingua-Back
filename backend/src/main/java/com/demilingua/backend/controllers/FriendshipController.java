package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/amigos")
public class FriendshipController {

    @PostMapping("/solicitar")
    public Map<String, String> enviarSolicitud(@RequestParam("emisorId") int emisorId, @RequestParam("receptorId") int receptorId) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO amistad (usuario_id_1, usuario_id_2, estado) VALUES (?, ?, 'PENDIENTE')";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, emisorId);
            ps.setInt(2, receptorId);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) {
            res.put("status", "error");
            res.put("message", "Ya existe una relación o error de conexión");
        }
        return res;
    }

    @PutMapping("/aceptar")
    public Map<String, String> aceptarSolicitud(@RequestParam("emisorId") int emisorId, @RequestParam("receptorId") int receptorId) {
        Map<String, String> res = new HashMap<>();
        String sql = "UPDATE amistad SET estado = 'ACEPTADO' WHERE usuario_id_1 = ? AND usuario_id_2 = ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, emisorId);
            ps.setInt(2, receptorId);
            if (ps.executeUpdate() > 0) res.put("status", "ok");
            else res.put("status", "error");
        } catch (SQLException e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @GetMapping("/lista")
    public List<Map<String, String>> listarAmigos(@RequestParam("usuarioId") int usuarioId) {
        List<Map<String, String>> amigos = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre FROM usuario u " +
                "JOIN amistad a ON (u.id = a.usuario_id_1 OR u.id = a.usuario_id_2) " +
                "WHERE (a.usuario_id_1 = ? OR a.usuario_id_2 = ?) AND a.estado = 'ACEPTADO' AND u.id != ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, usuarioId);
            ps.setInt(3, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> amigo = new HashMap<>();
                amigo.put("id", String.valueOf(rs.getInt("id")));
                amigo.put("nombre", rs.getString("nombre"));
                amigos.add(amigo);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return amigos;
    }
}
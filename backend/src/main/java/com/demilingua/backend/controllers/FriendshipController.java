package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    // LEER Amigos de un usuario
    @GetMapping("/{usuarioId}")
    public List<Map<String, String>> getFriends(@PathVariable int usuarioId) {
        List<Map<String, String>> amigos = new ArrayList<>();
        String sql = "SELECT usuario_id_2 as amigo_id, estado FROM amistad WHERE usuario_id_1 = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("amigo_id", String.valueOf(rs.getInt("amigo_id")));
                row.put("estado", rs.getString("estado"));
                amigos.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return amigos;
    }

    // CREAR Solicitud de amistad
    @PostMapping
    public Map<String, String> addFriend(@RequestParam("usuarioId1") int id1, @RequestParam("usuarioId2") int id2) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO amistad (usuario_id_1, usuario_id_2, estado) VALUES (?, ?, 'PENDIENTE')";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // ACTUALIZAR (Aceptar solicitud)
    @PutMapping
    public Map<String, String> acceptFriend(@RequestParam("usuarioId1") int id1, @RequestParam("usuarioId2") int id2) {
        Map<String, String> res = new HashMap<>();
        String sql = "UPDATE amistad SET estado = 'ACEPTADO' WHERE usuario_id_1 = ? AND usuario_id_2 = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // BORRAR (Eliminar amigo o rechazar solicitud)
    @DeleteMapping
    public Map<String, String> deleteFriend(@RequestParam("usuarioId1") int id1, @RequestParam("usuarioId2") int id2) {
        Map<String, String> res = new HashMap<>();
        String sql = "DELETE FROM amistad WHERE usuario_id_1 = ? AND usuario_id_2 = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
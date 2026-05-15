package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        String correo = credentials.get("correo");
        String contrasena = credentials.get("contrasena");

        String sql = "SELECT id, nombre FROM usuario WHERE correo = ? AND contrasena = ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                response.put("status", "ok");
                response.put("message", "Login exitoso");
                response.put("user_id", rs.getInt("id"));
                response.put("nombre", rs.getString("nombre"));
            } else {
                response.put("status", "error");
                response.put("message", "Correo o contraseña incorrectos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Error interno del servidor");
        }

        return response;
    }
}
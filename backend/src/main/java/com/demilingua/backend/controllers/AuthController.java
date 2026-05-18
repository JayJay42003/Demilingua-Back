package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
        String contrasenaInput = credentials.get("contrasena");

        String sql = "SELECT id, nombre, contrasena FROM usuario WHERE correo = ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("contrasena");
                boolean passwordMatch = false;

                // Intentar verificar con BCrypt
                try {
                    if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
                        passwordMatch = BCrypt.checkpw(contrasenaInput, storedPassword);
                    } else {
                        // Soporte para contraseñas en texto plano legadas
                        passwordMatch = contrasenaInput.equals(storedPassword);
                    }
                } catch (Exception e) {
                    // Si falla BCrypt (por ejemplo, formato inválido), reintento con texto plano
                    passwordMatch = contrasenaInput.equals(storedPassword);
                }

                if (passwordMatch) {
                    response.put("status", "ok");
                    response.put("message", "Login exitoso");
                    response.put("user_id", rs.getInt("id"));
                    response.put("nombre", rs.getString("nombre"));
                } else {
                    response.put("status", "error");
                    response.put("message", "Correo o contraseña incorrectos");
                }
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
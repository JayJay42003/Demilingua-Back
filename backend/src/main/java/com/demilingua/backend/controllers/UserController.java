package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // LEER (Todos o por ID)
    @GetMapping
    public List<Map<String, String>> getAll() {
        List<Map<String, String>> users = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, vidas, racha_actual, division_id FROM usuario";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getInt("id")));
                row.put("nombre", rs.getString("nombre"));
                row.put("correo", rs.getString("correo"));
                row.put("vidas", String.valueOf(rs.getInt("vidas")));
                row.put("racha_actual", String.valueOf(rs.getInt("racha_actual")));
                users.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    @GetMapping("/{id}")
    public Map<String, String> getById(@PathVariable int id) {
        Map<String, String> user = new HashMap<>();
        String sql = "SELECT id, nombre, correo, vidas, racha_actual, division_id FROM usuario WHERE id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.put("id", String.valueOf(rs.getInt("id")));
                user.put("nombre", rs.getString("nombre"));
                user.put("correo", rs.getString("correo"));
                user.put("vidas", String.valueOf(rs.getInt("vidas")));
                user.put("racha_actual", String.valueOf(rs.getInt("racha_actual")));
                user.put("status", "ok");
            } else {
                user.put("status", "error");
            }
        } catch (SQLException e) { 
            e.printStackTrace();
            user.put("status", "error");
        }
        return user;
    }

    // CREAR (Registro básico)
    @PostMapping
    public Map<String, String> create(@RequestParam("nombre") String nombre, @RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO usuario (nombre, correo, contrasena, division_id) VALUES (?, ?, ?, 1)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Hashear la contraseña antes de guardarla
            String hashedPass = BCrypt.hashpw(contrasena, BCrypt.gensalt());
            
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, hashedPass);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                res.put("status", "ok");
                res.put("user_id", String.valueOf(rs.getInt(1)));
                res.put("nombre", nombre);
            } else {
                res.put("status", "error");
            }
        } catch (SQLException e) { 
            e.printStackTrace();
            res.put("status", "error"); 
        }
        return res;
    }

    // ACTUALIZAR (Ej: Cambiar nombre, correo o contraseña)
    @PutMapping("/{id}")
    public Map<String, String> update(@PathVariable int id, 
                                      @RequestParam("nombre") String nombre, 
                                      @RequestParam("correo") String correo,
                                      @RequestParam(value = "contrasena", required = false) String contrasena) {
        Map<String, String> res = new HashMap<>();
        
        StringBuilder sql = new StringBuilder("UPDATE usuario SET nombre = ?, correo = ?");
        if (contrasena != null && !contrasena.isEmpty()) {
            sql.append(", contrasena = ?");
        }
        sql.append(" WHERE id = ?");

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            ps.setString(1, nombre);
            ps.setString(2, correo);
            
            if (contrasena != null && !contrasena.isEmpty()) {
                String hashedPass = BCrypt.hashpw(contrasena, BCrypt.gensalt());
                ps.setString(3, hashedPass);
                ps.setInt(4, id);
            } else {
                ps.setInt(3, id);
            }
            
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { 
            e.printStackTrace();
            res.put("status", "error"); 
        }
        return res;
    }

    // BORRAR
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable int id) {
        Map<String, String> res = new HashMap<>();
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
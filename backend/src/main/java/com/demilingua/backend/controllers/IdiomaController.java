package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/idiomas")
public class IdiomaController {

    // LEER (Todos)
    @GetMapping
    public List<Map<String, String>> getAll() {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM idioma";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getInt("id")));
                row.put("nombre", rs.getString("nombre"));
                lista.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // CREAR
    @PostMapping
    public Map<String, String> create(@RequestParam("nombre") String nombre) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO idioma (nombre) VALUES (?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public Map<String, String> update(@PathVariable int id, @RequestParam("nombre") String nombre) {
        Map<String, String> res = new HashMap<>();
        String sql = "UPDATE idioma SET nombre = ? WHERE id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, id);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // BORRAR
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable int id) {
        Map<String, String> res = new HashMap<>();
        String sql = "DELETE FROM idioma WHERE id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
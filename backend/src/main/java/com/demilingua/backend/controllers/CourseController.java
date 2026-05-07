package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    // LEER (Por idioma)
    @GetMapping
    public List<Map<String, String>> getCursos(@RequestParam("idiomaId") int idiomaId) {
        List<Map<String, String>> cursos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, dificultad, idioma_id FROM curso WHERE idioma_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idiomaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getInt("id")));
                row.put("nombre", rs.getString("nombre"));
                row.put("descripcion", rs.getString("descripcion"));
                row.put("dificultad", rs.getString("dificultad"));
                row.put("idioma_id", String.valueOf(rs.getInt("idioma_id")));
                cursos.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return cursos;
    }

    // CREAR
    @PostMapping
    public Map<String, String> create(@RequestParam("idiomaId") int idiomaId, @RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion, @RequestParam("dificultad") String dificultad) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO curso (idioma_id, nombre, descripcion, dificultad) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idiomaId);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setString(4, dificultad);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // ACTUALIZAR y BORRAR (Resumido)
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable int id) {
        Map<String, String> res = new HashMap<>();
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM curso WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
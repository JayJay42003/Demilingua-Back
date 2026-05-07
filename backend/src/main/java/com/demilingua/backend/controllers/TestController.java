package com.demilingua.backend.controllers;

import com.demilingua.backend.DBConfig;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TestController {

    // LEER Test Aleatorio
    @GetMapping("/test")
    public Map<String,String> getRandomTest(@RequestParam("cursoId") int cursoId) {
        String sql = "SELECT id, curso_id, titulo FROM test WHERE curso_id = ? ORDER BY RAND() LIMIT 1";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String,String> test = new HashMap<>();
                test.put("id", String.valueOf(rs.getInt("id")));
                test.put("curso_id", String.valueOf(rs.getInt("curso_id")));
                test.put("titulo", rs.getString("titulo"));
                return test;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new HashMap<>();
    }

    // LEER Ejercicios de un Test
    @GetMapping("/exercises")
    public List<Map<String,String>> getEjercicios(@RequestParam("testId") int testId) {
        String sql = "SELECT e.id AS ejercicioId, e.tipo, e.puntuacion, o.contenido, o.respuesta_correcta, o.opciones "
                + "FROM ejercicio e JOIN objeto_ejercicio o ON o.ejercicio_id = e.id "
                + "WHERE e.test_id = ? ORDER BY e.id";
        List<Map<String,String>> ejercicios = new ArrayList<>();
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, testId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String,String> row = new LinkedHashMap<>();
                row.put("ejercicioId", String.valueOf(rs.getInt("ejercicioId")));
                row.put("tipo", rs.getString("tipo"));
                row.put("puntos", String.valueOf(rs.getInt("puntuacion")));
                row.put("contenido", rs.getString("contenido"));
                row.put("respuesta", rs.getString("respuesta_correcta"));
                row.put("opciones", rs.getString("opciones"));
                ejercicios.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ejercicios;
    }

    // CREAR TEST
    @PostMapping("/test")
    public Map<String, String> createTest(@RequestParam("cursoId") int cursoId, @RequestParam("titulo") String titulo) {
        Map<String, String> res = new HashMap<>();
        String sql = "INSERT INTO test (curso_id, titulo) VALUES (?, ?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cursoId);
            ps.setString(2, titulo);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }

    // BORRAR TEST
    @DeleteMapping("/test/{id}")
    public Map<String, String> deleteTest(@PathVariable int id) {
        Map<String, String> res = new HashMap<>();
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM test WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            res.put("status", "ok");
        } catch (SQLException e) { res.put("status", "error"); }
        return res;
    }
}
package com.demilingua.backend.services;

import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.repositories.UsuarioRepository;
import com.demilingua.backend.repositories.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Map<String, String>> getAllUsers() {
        return usuarioRepository.findAll().stream().map(this::mapUserToMap).collect(Collectors.toList());
    }

    public Map<String, String> getUserById(int id) {
        return usuarioRepository.findById(id)
                .map(u -> {
                    Map<String, String> m = mapUserToMap(u);
                    m.put("status", "ok");
                    return m;
                }).orElseGet(() -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("status", "error");
                    return m;
                });
    }

    @Transactional
    public Map<String, String> createUser(String nombre, String correo, String contrasena) {
        Map<String, String> res = new HashMap<>();
        try {
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setCorreo(correo);
            u.setContrasena(passwordEncoder.encode(contrasena));
            u.setDivision(divisionRepository.findById(1).orElse(null));
            
            Usuario saved = usuarioRepository.save(u);
            res.put("status", "ok");
            res.put("user_id", String.valueOf(saved.getId()));
            res.put("nombre", saved.getNombre());
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }

    @Transactional
    public Map<String, String> updateUser(int id, String nombre, String correo, String contrasena) {
        Map<String, String> res = new HashMap<>();
        try {
            Usuario u = usuarioRepository.findById(id).orElseThrow();
            u.setNombre(nombre);
            u.setCorreo(correo);
            if (contrasena != null && !contrasena.isEmpty()) {
                u.setContrasena(passwordEncoder.encode(contrasena));
            }
            usuarioRepository.save(u);
            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }

    @Transactional
    public Map<String, String> deleteUser(int id) {
        Map<String, String> res = new HashMap<>();
        try {
            usuarioRepository.deleteById(id);
            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }

    private Map<String, String> mapUserToMap(Usuario u) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(u.getId()));
        map.put("nombre", u.getNombre());
        map.put("correo", u.getCorreo());
        map.put("vidas", String.valueOf(u.getVidas()));
        map.put("racha_actual", String.valueOf(u.getRachaActual()));
        return map;
    }
}
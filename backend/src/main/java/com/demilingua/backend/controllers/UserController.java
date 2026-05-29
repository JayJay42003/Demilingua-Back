package com.demilingua.backend.controllers;

import com.demilingua.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestParam("nombre") String nombre, @RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena) {
        return ResponseEntity.ok(userService.createUser(nombre, correo, contrasena));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable int id, 
                                                      @RequestParam("nombre") String nombre, 
                                                      @RequestParam("correo") String correo,
                                                      @RequestParam(value = "contrasena", required = false) String contrasena) {
        return ResponseEntity.ok(userService.updateUser(id, nombre, correo, contrasena));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}

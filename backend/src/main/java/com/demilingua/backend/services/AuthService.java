package com.demilingua.backend.services;

import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.repositories.UsuarioRepository;
import com.demilingua.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> authenticateUser(String correo, String contrasena) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> optionalUser = usuarioRepository.findByCorreo(correo);
            if (optionalUser.isPresent()) {
                Usuario usuario = optionalUser.get();
                
                // Fallback para contraseñas en texto plano (Legacy Support)
                if (!usuario.getContrasena().startsWith("$2a$") && !usuario.getContrasena().startsWith("$2b$")) {
                     if (usuario.getContrasena().equals(contrasena)) {
                         // Actualizar a BCrypt automáticamente si el login plano fue exitoso
                         usuario.setContrasena(passwordEncoder.encode(contrasena));
                         usuarioRepository.save(usuario);
                     } else {
                         throw new IllegalArgumentException("Credenciales inválidas");
                     }
                }

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(correo, contrasena)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = tokenProvider.generateToken(correo);

                response.put("status", "ok");
                response.put("message", "Login exitoso");
                response.put("token", jwt);
                response.put("user_id", usuario.getId());
                response.put("nombre", usuario.getNombre());
            } else {
                throw new IllegalArgumentException("Credenciales inválidas");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Correo o contraseña incorrectos");
        }

        return response;
    }
}
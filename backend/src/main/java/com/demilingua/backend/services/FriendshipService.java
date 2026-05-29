package com.demilingua.backend.services;

import com.demilingua.backend.entities.Amistad;
import com.demilingua.backend.entities.AmistadId;
import com.demilingua.backend.entities.Usuario;
import com.demilingua.backend.repositories.AmistadRepository;
import com.demilingua.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
public class FriendshipService {

    @Autowired
    private AmistadRepository amistadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Map<String, String>> getFriends(int usuarioId) {
        List<Map<String, String>> result = new ArrayList<>();
        List<Amistad> friendships = amistadRepository.findAcceptedFriendships(usuarioId);
        friendships.add((Amistad) amistadRepository.findPendingRequests(usuarioId));

        for (Amistad a : friendships) {
            Usuario amigo = (a.getUsuario1().getId() == usuarioId) ? a.getUsuario2() : a.getUsuario1();
            
            Map<String, String> row = new HashMap<>();
            row.put("amigo_id", String.valueOf(amigo.getId()));
            row.put("estado", a.getEstado().name());
            row.put("nombre", amigo.getNombre());
            
            row.put("puntos", "0");
            result.add(row);
        }
        return result;
    }

    @Transactional
    public Map<String, String> addFriend(int id1, int id2) {
        Map<String, String> res = new HashMap<>();
        try {
            AmistadId id = new AmistadId(id1, id2);
            if (amistadRepository.existsById(id)) {
                res.put("status", "error");
                res.put("message", "La solicitud ya existe");
                return res;
            }

            Amistad amistad = new Amistad();
            amistad.setId(id);
            amistad.setUsuario1(usuarioRepository.findById(id1).orElseThrow(() -> new NoSuchElementException("Usuario 1 no encontrado")));
            amistad.setUsuario2(usuarioRepository.findById(id2).orElseThrow(() -> new NoSuchElementException("Usuario 2 no encontrado")));
            amistad.setEstado(Amistad.EstadoAmistad.PENDIENTE);
            
            amistadRepository.save(amistad);
            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }

    @Transactional
    public Map<String, String> acceptFriend(int id1, int id2) {
        Map<String, String> res = new HashMap<>();
        try {
            AmistadId id = new AmistadId(id1, id2);
            Optional<Amistad> opt = amistadRepository.findById(id);
            if (opt.isPresent()) {
                Amistad a = opt.get();
                a.setEstado(Amistad.EstadoAmistad.ACEPTADO);
                amistadRepository.save(a);
                res.put("status", "ok");
            } else {
                res.put("status", "error");
            }
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }

    @Transactional
    public Map<String, String> deleteFriend(int id1, int id2) {
        Map<String, String> res = new HashMap<>();
        try {
            AmistadId id = new AmistadId(id1, id2);
            amistadRepository.deleteById(id);
            res.put("status", "ok");
        } catch (Exception e) {
            res.put("status", "error");
        }
        return res;
    }
}
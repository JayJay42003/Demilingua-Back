package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findTop10ByOrderByRachaActualDesc();

    List<Usuario> findByDivisionIdOrderByRachaActualDesc(Integer divisionId);
}

package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.UsuarioIdioma;
import com.demilingua.backend.entities.UsuarioIdiomaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioIdiomaRepository extends JpaRepository<UsuarioIdioma, UsuarioIdiomaId> {
    List<UsuarioIdioma> findByUsuarioId(Integer usuarioId);
}
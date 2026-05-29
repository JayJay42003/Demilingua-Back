package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.UsuarioTest;
import com.demilingua.backend.entities.UsuarioTestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioTestRepository extends JpaRepository<UsuarioTest, UsuarioTestId> {
    List<UsuarioTest> findByUsuarioId(Integer usuarioId);
}

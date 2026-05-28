package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByIdiomaId(Integer idiomaId);
}
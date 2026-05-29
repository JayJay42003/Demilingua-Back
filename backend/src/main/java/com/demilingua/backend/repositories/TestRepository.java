package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findByCursoId(Integer cursoId);
}

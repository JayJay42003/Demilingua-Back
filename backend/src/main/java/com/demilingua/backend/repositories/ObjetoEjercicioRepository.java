package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.ObjetoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ObjetoEjercicioRepository extends JpaRepository<ObjetoEjercicio, Integer> {
    List<ObjetoEjercicio> findByEjercicioId(Integer ejercicioId);
}

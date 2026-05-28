package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {
}
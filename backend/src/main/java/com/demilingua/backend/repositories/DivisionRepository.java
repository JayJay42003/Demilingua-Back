package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Integer> {
}
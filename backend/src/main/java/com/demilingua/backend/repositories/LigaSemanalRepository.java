package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.LigaSemanal;
import com.demilingua.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LigaSemanalRepository extends JpaRepository<LigaSemanal, Integer> {
    Optional<LigaSemanal> findByUsuarioAndFechaInicioSemana(Usuario usuario, Date fechaInicioSemana);
    
    // Para obtener el ranking de la liga actual
    List<LigaSemanal> findByFechaInicioSemanaOrderByXpSemanalDesc(Date fechaInicioSemana);
}

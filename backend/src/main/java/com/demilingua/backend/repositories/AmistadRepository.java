package com.demilingua.backend.repositories;

import com.demilingua.backend.entities.Amistad;
import com.demilingua.backend.entities.AmistadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AmistadRepository extends JpaRepository<Amistad, AmistadId> {

    @Query("SELECT a FROM Amistad a WHERE (a.usuario1.id = :uid OR a.usuario2.id = :uid) AND a.estado = 'ACEPTADO'")
    List<Amistad> findAcceptedFriendships(@Param("uid") Integer usuarioId);

    @Query("SELECT a FROM Amistad a WHERE a.usuario2.id = :uid AND a.estado = 'PENDIENTE'")
    List<Amistad> findPendingRequests(@Param("uid") Integer usuarioId);
}

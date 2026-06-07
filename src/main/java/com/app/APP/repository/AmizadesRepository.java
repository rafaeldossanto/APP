package com.app.APP.repository;

import com.app.APP.entity.Amizades;
import com.app.APP.model.enums.StatusAmizade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmizadesRepository extends JpaRepository<Amizades, String> {

    @Query("SELECT a FROM Amizades a WHERE (a.solicitanteId = :userId OR a.receptorId = :userId) AND a.status = :status")
    List<Amizades> findByUsuarioIdAndStatus(String userId, StatusAmizade status);

    @Query("SELECT a FROM Amizades a WHERE (a.solicitanteId = :id1 AND a.receptorId = :id2) OR (a.solicitanteId = :id2 AND a.receptorId = :id1)")
    Optional<Amizades> findRelacao(String id1, String id2);
}
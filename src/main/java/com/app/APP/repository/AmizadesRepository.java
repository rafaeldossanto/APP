package com.app.APP.repository;

import com.app.APP.entity.Amizades;
import com.app.APP.model.enums.StatusAmizade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmizadesRepository extends JpaRepository<Amizades, String> {

    /** Relacoes de um usuario (em qualquer ponta) num dado status — ex.: amigos (ACEITA). */
    @Query(value = "SELECT a FROM Amizades a WHERE (a.solicitanteId = :userId OR a.receptorId = :userId) AND a.status = :status",
            countQuery = "SELECT COUNT(a) FROM Amizades a WHERE (a.solicitanteId = :userId OR a.receptorId = :userId) AND a.status = :status")
    Page<Amizades> findByUsuarioIdAndStatus(String userId, StatusAmizade status, Pageable pageable);

    /** Solicitacoes que CHEGARAM ao usuario (ele e o receptor). */
    Page<Amizades> findByReceptorIdAndStatus(String receptorId, StatusAmizade status, Pageable pageable);

    /** Solicitacoes que o usuario ENVIOU (ele e o solicitante). */
    Page<Amizades> findBySolicitanteIdAndStatus(String solicitanteId, StatusAmizade status, Pageable pageable);

    @Query("SELECT a FROM Amizades a WHERE (a.solicitanteId = :id1 AND a.receptorId = :id2) OR (a.solicitanteId = :id2 AND a.receptorId = :id1)")
    Optional<Amizades> findRelacao(String id1, String id2);

    /** Ids dos amigos do usuario (a outra ponta das relacoes no status dado). */
    @Query("""
            SELECT CASE WHEN a.solicitanteId = :usuarioId THEN a.receptorId ELSE a.solicitanteId END
            FROM Amizades a
            WHERE (a.solicitanteId = :usuarioId OR a.receptorId = :usuarioId) AND a.status = :status
            """)
    List<String> findAmigoIds(String usuarioId, StatusAmizade status);
}

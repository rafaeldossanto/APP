package com.app.APP.repository;

import com.app.APP.entity.Aventura;
import com.app.APP.model.enums.StatusAventura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AventuraRepository extends JpaRepository<Aventura, String> {
    Page<Aventura> findByUsuarioId(String usuarioId, Pageable pageable);
    List<Aventura> findByUsuarioIdAndStatus(String usuarioId, StatusAventura status);

    /** Ao apagar a regiao (pasta), as aventuras nao sao apagadas — so desvinculadas. */
    @Modifying
    @Query("UPDATE Aventura a SET a.regiao = null WHERE a.regiao.id = :regiaoId")
    void desvincularRegiao(String regiaoId);

    /**
     * Aventuras de uma regiao visiveis ao observador: PUBLICA para todos, as
     * proprias sempre, SO_GRUPO apenas se ele participa. A pasta nunca expoe uma
     * aventura que o observador nao veria sozinho.
     */
    @Query("""
            SELECT a FROM Aventura a
            WHERE a.regiao.id = :regiaoId AND (
                a.visibilidade = com.app.APP.model.enums.VisibilidadeAventura.PUBLICA
                OR a.usuarioId = :observador
                OR (a.visibilidade = com.app.APP.model.enums.VisibilidadeAventura.SO_GRUPO
                    AND EXISTS (SELECT 1 FROM ParticipanteAventura p WHERE p.aventura.id = a.id AND p.usuarioId = :observador))
            )
            """)
    Page<Aventura> findVisiveisNaRegiao(String regiaoId, String observador, Pageable pageable);
}
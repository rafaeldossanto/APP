package com.app.APP.repository;

import com.app.APP.entity.Regiao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegiaoRepository extends JpaRepository<Regiao, String> {

    Page<Regiao> findByUsuarioId(String usuarioId, Pageable pageable);

    /**
     * Regioes visiveis ao solicitante que NAO sao dele: PUBLICA de qualquer um,
     * ou AMIGOS cujo dono esteja na lista de amigos.
     */
    @Query("""
            SELECT r FROM Regiao r
            WHERE r.usuarioId <> :usuarioId AND (
                r.visibilidade = com.app.APP.model.enums.VisibilidadeRegiao.PUBLICA
                OR (r.visibilidade = com.app.APP.model.enums.VisibilidadeRegiao.AMIGOS AND r.usuarioId IN :amigoIds)
            )
            """)
    Page<Regiao> findDescobriveis(String usuarioId, List<String> amigoIds, Pageable pageable);
}

package com.app.APP.repository;

import com.app.APP.entity.Seguidor;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeguidorRepository extends JpaRepository<Seguidor, String> {

    boolean existsBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);

    void deleteBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);

    /** Quantos seguem o usuario. */
    long countBySeguidoId(String seguidoId);

    /** Quantos o usuario segue. */
    long countBySeguidorId(String seguidorId);

    /** Quem segue o usuario (visao publica). */
    @Query(value = "SELECT new com.app.APP.model.dto.response.UsuarioPublicoResponse(u.codigoUsuario, u.nome) "
            + "FROM Seguidor s, Usuario u WHERE u.id = s.seguidorId AND s.seguidoId = :usuarioId",
            countQuery = "SELECT COUNT(s) FROM Seguidor s WHERE s.seguidoId = :usuarioId")
    Page<UsuarioPublicoResponse> findSeguidores(String usuarioId, Pageable pageable);

    /** Quem o usuario segue (visao publica). */
    @Query(value = "SELECT new com.app.APP.model.dto.response.UsuarioPublicoResponse(u.codigoUsuario, u.nome) "
            + "FROM Seguidor s, Usuario u WHERE u.id = s.seguidoId AND s.seguidorId = :usuarioId",
            countQuery = "SELECT COUNT(s) FROM Seguidor s WHERE s.seguidorId = :usuarioId")
    Page<UsuarioPublicoResponse> findSeguindo(String usuarioId, Pageable pageable);
}

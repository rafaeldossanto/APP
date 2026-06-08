package com.app.APP.repository;

import com.app.APP.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByCodigoUsuario(String codigoUsuario);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.codigoUsuario) LIKE LOWER(CONCAT(:termo, '%'))")
    List<Usuario> buscarPorPrefixoCodigo(@Param("termo") String termo);
}

package com.app.APP.repository;

import com.app.APP.entity.Caminho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaminhoRepository extends JpaRepository<Caminho, String> {
    Page<Caminho> findByAventuraId(String aventuraId, Pageable pageable);
    Page<Caminho> findByUsuarioId(String usuarioId, Pageable pageable);
}
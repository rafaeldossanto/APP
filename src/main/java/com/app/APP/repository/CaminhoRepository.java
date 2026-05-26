package com.app.APP.repository;

import com.app.APP.entity.Caminho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaminhoRepository extends JpaRepository<Caminho, String> {
    List<Caminho> findByAventuraId(String aventuraId);
    List<Caminho> findByUsuarioId(String usuarioId);
}
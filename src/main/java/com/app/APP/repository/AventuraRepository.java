package com.app.APP.repository;

import com.app.APP.entity.Aventura;
import com.app.APP.model.enums.StatusAventura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AventuraRepository extends JpaRepository<Aventura, String> {
    List<Aventura> findByUsuarioId(String usuarioId);
    List<Aventura> findByUsuarioIdAndStatus(String usuarioId, StatusAventura status);
}
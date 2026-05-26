package com.app.APP.repository;

import com.app.APP.entity.ParticipanteAventura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteAventuraRepository extends JpaRepository<ParticipanteAventura, String> {
    boolean existsByAventuraIdAndUsuarioId(String aventuraId, String usuarioId);
}

package com.app.APP.repository;

import com.app.APP.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, String> {
    List<Evidencia> findByPontoId(String pontoId);

    @Query("SELECT COUNT(DISTINCT e.usuarioId) FROM Evidencia e WHERE e.ponto.id = :pontoId AND e.validada = true")
    Long countUsuariosValidadosByPontoId(String pontoId);

    /**
     * Conta usuarios validados distintos para VARIOS pontos de uma 
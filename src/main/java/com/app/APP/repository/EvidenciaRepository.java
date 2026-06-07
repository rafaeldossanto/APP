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
     * Conta usuarios validados distintos para VARIOS pontos de uma vez, evitando
     * o N+1 ao listar pontos de um caminho. Retorna [pontoId, total] por linha.
     */
    @Query("SELECT e.ponto.id, COUNT(DISTINCT e.usuarioId) FROM Evidencia e " +
            "WHERE e.ponto.id IN :pontoIds AND e.validada = true GROUP BY e.ponto.id")
    List<Object[]> countUsuariosValidadosPorPontos(List<String> pontoIds);

    boolean existsByPontoIdAndUsuarioId(String pontoId, String usuarioId);
}
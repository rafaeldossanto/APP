package com.app.APP.repository;

import com.app.APP.entity.Midia;
import com.app.APP.model.enums.TipoMidia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, String> {
    List<Midia> findByAventuraId(String aventuraId);
    List<Midia> findByCaminhoId(String caminhoId);
    List<Midia> findByAventuraIdAndTipo(String aventuraId, TipoMidia tipo);
}
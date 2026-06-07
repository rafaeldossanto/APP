package com.app.APP.repository;

import com.app.APP.entity.Midia;
import com.app.APP.model.enums.TipoMidia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, String> {
    Page<Midia> findByAventuraId(String aventuraId, Pageable pageable);
    Page<Midia> findByCaminhoId(String caminhoId, Pageable pageable);
    List<Midia> findByAventuraIdAndTipo(String aventuraId, TipoMidia tipo);
}
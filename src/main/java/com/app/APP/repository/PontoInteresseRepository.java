package com.app.APP.repository;

import com.app.APP.entity.PontoInteresse;
import com.app.APP.model.enums.TipoPonto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoInteresseRepository extends JpaRepository<PontoInteresse, String> {
    Page<PontoInteresse> findByCaminhoId(String caminhoId, Pageable pageable);
    List<PontoInteresse> findByCaminhoIdAndTipo(String caminhoId, TipoPonto tipo);
}

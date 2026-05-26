package com.app.APP.repository;

import com.app.APP.entity.PontoInteresse;
import com.app.APP.model.enums.TipoPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoInteresseRepository extends JpaRepository<PontoInteresse, String> {
    List<PontoInteresse> findByCaminhoId(String caminhoId);
    List<PontoInteresse> findByCaminhoIdAndTipo(String caminhoId, TipoPonto tipo);
}

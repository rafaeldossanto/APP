package com.app.APP.stub;

import com.app.APP.entity.Caminho;
import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.enums.Cores;

import java.time.LocalDateTime;

/**
 * Facilitador de testes para Caminho.
 */
public final class CaminhoStub {

    public static final String ID = "caminho-1";
    public static final String USUARIO_ID = "usuario-1";
    public static final Integer NUMERO = 1;

    private CaminhoStub() {
    }

    public static Caminho.CaminhoBuilder umCaminho() {
        return Caminho.builder()
                .id(ID)
                .aventura(AventuraStub.umaAventura().build())
                .usuarioId(USUARIO_ID)
                .cor(Cores.ROXO)
                .numero(NUMERO)
                .iniciadoEm(LocalDateTime.now());
    }

    public static CaminhoRequest umRequest() {
        return new CaminhoRequest(AventuraStub.ID, Cores.ROXO);
    }
}

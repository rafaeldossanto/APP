package com.app.APP.stub;

import com.app.APP.entity.Amizades;
import com.app.APP.model.dto.request.AmizadeRequest;
import com.app.APP.model.enums.StatusAmizade;

import java.time.LocalDateTime;

/**
 * Facilitador de testes para Amizades.
 */
public final class AmizadeStub {

    public static final String ID = "amizade-1";
    public static final String SOLICITANTE_ID = "usuario-1";
    public static final String RECEPTOR_ID = "usuario-2";
    public static final String RECEPTOR_CODIGO = "rafael#2";

    private AmizadeStub() {
    }

    public static Amizades.AmizadesBuilder umaAmizade() {
        return Amizades.builder()
                .id(ID)
                .solicitanteId(SOLICITANTE_ID)
                .receptorId(RECEPTOR_ID)
                .status(StatusAmizade.PENDENTE)
                .solicitadoEm(LocalDateTime.now());
    }

    public static AmizadeRequest umRequest() {
        return new AmizadeRequest(RECEPTOR_CODIGO);
    }
}

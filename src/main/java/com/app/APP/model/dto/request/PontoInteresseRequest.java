package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoPonto;

public record PontoInteresseRequest(
        String caminhoId,
        String usuarioId,
        TipoPonto tipo,
        String nome,
        String descricao,
        Double latitude,
        Double longitude
) {}
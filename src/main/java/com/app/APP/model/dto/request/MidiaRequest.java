package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoMidia;

public record MidiaRequest(
        String aventuraId,
        String caminhoId,
        String usuarioId,
        TipoMidia tipo,
        String url,
        Double latCaptura,
        Double lngCaptura,
        Double distanciaNaCapturaKm,
        Double percentualNoCaminho
) {}
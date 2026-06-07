package com.app.APP.model.dto.response;

import com.app.APP.model.enums.TipoMidia;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MidiaResponse(
        String id,
        String aventuraId,
        String caminhoId,
        TipoMidia tipo,
        String url,
        Double percentualNoCaminho,
        Double distanciaNaCapturaKm,
        LocalDateTime capturadaEm
) {}
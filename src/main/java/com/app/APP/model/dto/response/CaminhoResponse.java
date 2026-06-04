package com.app.APP.model.dto.response;

import com.app.APP.model.enums.Cores;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CaminhoResponse(
        String id,
        String aventuraId,
        String usuarioId,
        Cores cor,
        Integer numero,
        LocalDateTime iniciadoEm,
        LocalDateTime finalizadoEm,
        Double distanciaTotalKm
) {}
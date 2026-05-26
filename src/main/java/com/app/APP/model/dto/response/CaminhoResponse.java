package com.app.APP.model.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CaminhoResponse(
        String id,
        String aventuraId,
        String usuarioId,
        String cor,
        Integer numero,
        LocalDateTime iniciadoEm,
        LocalDateTime finalizadoEm,
        Double distanciaTotalKm
) {}
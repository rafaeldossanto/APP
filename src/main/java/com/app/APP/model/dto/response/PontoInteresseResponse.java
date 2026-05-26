package com.app.APP.model.dto.response;

import com.app.APP.model.enums.TipoPonto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PontoInteresseResponse(
        String id,
        String caminhoId,
        String usuarioId,
        TipoPonto tipo,
        String nome,
        String descricao,
        Double latitude,
        Double longitude,
        Integer nivelConfianca,
        LocalDateTime criadoEm
) {}
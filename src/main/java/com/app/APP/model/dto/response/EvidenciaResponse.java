package com.app.APP.model.dto.response;

import com.app.APP.model.enums.TipoEvidencia;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EvidenciaResponse(
        String id,
        String pontoId,
        String usuarioId,
        String fotoUrl,
        TipoEvidencia tipoEvidencia,
        Boolean validada,
        LocalDateTime criadoEm
) {}
package com.app.APP.model.dto.response;

import com.app.APP.model.enums.StatusAmizade;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AmizadeResponse(
        String id,
        String solicitanteId,
        String receptorId,
        StatusAmizade status,
        LocalDateTime solicitadoEm,
        LocalDateTime respondidoEm
) {}
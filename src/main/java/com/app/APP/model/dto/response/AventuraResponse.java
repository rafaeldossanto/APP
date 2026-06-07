package com.app.APP.model.dto.response;

import com.app.APP.model.enums.StatusAventura;
import com.app.APP.model.enums.VisibilidadeAventura;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AventuraResponse(
        String id,
        String usuarioId,
        String regiaoId,
        String destino,
        StatusAventura status,
        VisibilidadeAventura visibilidade,
        LocalDateTime criadoEm
) {}
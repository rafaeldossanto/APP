package com.app.APP.model.dto.request;

import com.app.APP.model.enums.VisibilidadeAventura;

public record AventuraRequest(
        String usuarioId,
        String regiaoId,
        String destino,
        VisibilidadeAventura visibilidade
) {}
package com.app.APP.model.dto.request;

import com.app.APP.model.enums.Cores;

public record CaminhoRequest(
        String aventuraId,
        String usuarioId,
        Cores cor,
        Integer numero
) {}
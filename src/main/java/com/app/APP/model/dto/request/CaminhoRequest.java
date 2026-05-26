package com.app.APP.model.dto.request;

public record CaminhoRequest(
        String aventuraId,
        String usuarioId,
        String cor,
        Integer numero
) {}
package com.app.APP.model.dto.response;

/** Contadores de seguidores/seguindo de um usuario. */
public record ContadoresResponse(
        long seguidores,
        long seguindo
) {}

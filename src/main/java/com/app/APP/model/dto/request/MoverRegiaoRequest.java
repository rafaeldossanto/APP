package com.app.APP.model.dto.request;

/**
 * Move uma aventura entre pastas. regiaoId nulo = tirar a aventura da pasta.
 */
public record MoverRegiaoRequest(
        String regiaoId
) {}

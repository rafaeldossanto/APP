package com.app.APP.model.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * O ator (solicitante/bloqueador) NAO vem no request — e derivado do token.
 * O cliente informa apenas o alvo da operacao.
 */
public record AmizadeRequest(
        @NotBlank String receptorId
) {}

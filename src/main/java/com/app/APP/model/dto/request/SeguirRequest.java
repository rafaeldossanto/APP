package com.app.APP.model.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Alvo de seguir/deixar de seguir, pelo codigoUsuario (no corpo — evita o '#'
 * no path). O seguidor vem do token.
 */
public record SeguirRequest(
        @NotBlank String seguidoCodigo
) {}

package com.app.APP.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AmizadeRequest(
        @NotBlank String solicitanteId,
        @NotBlank String receptorId
) {}

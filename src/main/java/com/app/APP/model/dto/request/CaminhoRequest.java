package com.app.APP.model.dto.request;

import com.app.APP.model.enums.Cores;
import jakarta.validation.constraints.NotBlank;

/**
 * Inicio de caminho. Cor (default ROXO) e numero sao opcionais; aventura e
 * usuario sao obrigatorios.
 */
public record CaminhoRequest(
        @NotBlank String aventuraId,
        @NotBlank String usuarioId,
        Cores cor,
        Integer numero
) {}

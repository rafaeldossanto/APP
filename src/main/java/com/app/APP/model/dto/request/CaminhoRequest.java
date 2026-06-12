package com.app.APP.model.dto.request;

import com.app.APP.model.enums.Cores;
import jakarta.validation.constraints.NotBlank;

/**
 * Inicio de caminho. Aventura e obrigatoria; cor e opcional (default ROXO).
 * O dono vem do token (nao do corpo). O numero do caminho NAO vem do cliente —
 * e gerado de forma sequencial por aventura no service.
 */
public record CaminhoRequest(
        @NotBlank String aventuraId,
        Cores cor
) {}

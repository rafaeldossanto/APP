package com.app.APP.model.dto.request;

import com.app.APP.model.enums.VisibilidadeAventura;
import jakarta.validation.constraints.NotBlank;

/**
 * Criacao de aventura. A visibilidade e opcional (a entidade tem default
 * PRIVADA); regiao e destino sao obrigatorios. O dono vem do token, nao do corpo.
 */
public record AventuraRequest(
        @NotBlank String regiaoId,
        @NotBlank String destino,
        VisibilidadeAventura visibilidade
) {}

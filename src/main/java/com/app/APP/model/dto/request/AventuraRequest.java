package com.app.APP.model.dto.request;

import com.app.APP.model.enums.VisibilidadeAventura;
import jakarta.validation.constraints.NotBlank;

/**
 * Criacao de aventura. regiao (pasta) e opcional — aventura pode existir solta;
 * destino e obrigatorio. Visibilidade opcional (entidade tem default PRIVADA).
 * O dono vem do token, nao do corpo.
 */
public record AventuraRequest(
        String regiaoId,
        @NotBlank String destino,
        VisibilidadeAventura visibilidade
) {}

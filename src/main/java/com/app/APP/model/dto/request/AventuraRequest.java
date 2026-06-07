package com.app.APP.model.dto.request;

import com.app.APP.model.enums.VisibilidadeAventura;
import jakarta.validation.constraints.NotBlank;

/**
 * Criacao de aventura. A visibilidade e opcional (a entidade tem default
 * PRIVADA); os demais campos identificam o dono e o destino, obrigatorios.
 */
public record AventuraRequest(
        @NotBlank String usuarioId,
        @NotBlank String regiaoId,
        @NotBlank String destino,
        VisibilidadeAventura visibilidade
) {}

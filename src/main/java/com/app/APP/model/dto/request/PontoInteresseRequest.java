package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Criacao de ponto de interesse. Nome e descricao sao opcionais (a descricao,
 * inclusive, eleva o nivel de confianca, mas nao e obrigatoria). Coordenadas
 * e tipo identificam o ponto e sao obrigatorios.
 */
public record PontoInteresseRequest(
        @NotBlank String caminhoId,
        @NotBlank String usuarioId,
        @NotNull TipoPonto tipo,
        String nome,
        String descricao,
        @NotNull Double latitude,
        @NotNull Double longitude
) {}

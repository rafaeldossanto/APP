package com.app.APP.model.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Cidade de uma regiao (usado tanto no request quanto no response). Coordenadas
 * sao opcionais.
 */
public record CidadeDTO(
        @NotBlank String nome,
        Double latitude,
        Double longitude,
        Double altitude
) {}

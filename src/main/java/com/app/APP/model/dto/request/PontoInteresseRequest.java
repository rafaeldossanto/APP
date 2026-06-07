package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Criacao de ponto de interesse. Nome e descricao sao opcionais (a descricao,
 * inclusive, eleva o nivel de confianca, mas na
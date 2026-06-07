package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoEvidencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Adicao de evidencia a um ponto. As coordenadas de captura sao obrigatorias —
 * o service valida que estao 
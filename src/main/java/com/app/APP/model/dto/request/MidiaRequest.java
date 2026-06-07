package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoMidia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Salva os metadados de uma midia. caminhoId e opcional (midia avulsa na
 * aventura); os campos de captura GPS sao opcionais. aventura, usuario, tipo
 * e url (do binario ja no storage) sao obri
package com.app.APP.model.dto.request;

import com.app.APP.model.enums.VisibilidadeRegiao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Criacao/edicao de regiao (pasta). O dono vem do token. Visibilidade nula vira
 * PRIVADA (default seguro). cidades pode vir vazia.
 */
public record RegiaoRequest(
        @NotBlank String nome,
        String descricao,
        VisibilidadeRegiao visibilidade,
        @Valid List<CidadeDTO> cidades
) {}

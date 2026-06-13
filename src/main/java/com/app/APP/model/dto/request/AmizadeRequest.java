package com.app.APP.model.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * O ator (solicitante/bloqueador) NAO vem no request — e derivado do token.
 * O cliente informa o alvo pelo codigoUsuario (handle publico); o service
 * resolve para o id interno. Assim nao expomos o UUID na busca de usuarios.
 */
public record AmizadeRequest(
        @NotBlank String receptorCodigo
) {}

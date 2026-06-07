package com.app.APP.model.dto.response;

import lombok.Builder;

/**
 * Visao publica de um usuario, exposta na busca para adicionar amigos.
 * Inclui apenas o que outro usuario pode ver — nome e codigoUsuario.
 * NAO expoe email nem id interno, por privacidade.
 */
@Builder
public record UsuarioPublicoResponse(
        String codigoUsuario,
        String nome
) {}

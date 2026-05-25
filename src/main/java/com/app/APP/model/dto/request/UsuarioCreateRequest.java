package com.app.APP.model.dto.request;

public record UsuarioCreateRequest(
        String nome,
        String email,
        String senha
) {
}

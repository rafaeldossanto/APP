package com.app.APP.auth;

/**
 * Identidade do usuario autenticado, extraida dos claims do JWT validado.
 * Injetada nos controllers pelo {@link UsuarioAutenticadoArgumentResolver} —
 * o ator nunca vem do corpo/path da requisicao.
 */
public record UsuarioAutenticado(String id, String codigoUsuario, String email) {
}

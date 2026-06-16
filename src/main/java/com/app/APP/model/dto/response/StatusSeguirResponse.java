package com.app.APP.model.dto.response;

/**
 * Relacao de seguir entre o usuario do token e outro: se eu o sigo, se ele me
 * segue e se e mutuo (mutuo libera o botao de adicionar amigo).
 */
public record StatusSeguirResponse(
        boolean sigo,
        boolean meSegue,
        boolean mutuo
) {}

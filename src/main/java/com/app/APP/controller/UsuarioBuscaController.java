package com.app.APP.controller;

import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.service.UsuarioBuscaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Busca de usuarios pelo codigoUsuario publico, para adicionar amigos.
 */
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioBuscaController {

    private final UsuarioBuscaService usuarioBuscaService;

    @GetMapping("/codigo/{codigoUsuario}")
    public UsuarioPublicoResponse buscarPorCodigo(@PathVariable String codigoUsuario) {
        return usuarioBuscaService.buscarPorCodigo(codigoUsuario);
    }

    @GetMapping("/busca")
    public List<UsuarioPublicoResponse> autocomplete(@RequestParam String termo) {
        return usuarioBuscaService.autocomplete(termo);
    }
}

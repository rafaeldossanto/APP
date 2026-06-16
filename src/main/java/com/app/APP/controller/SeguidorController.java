package com.app.APP.controller;

import com.app.APP.auth.UsuarioAutenticado;
import com.app.APP.model.dto.response.ContadoresResponse;
import com.app.APP.model.dto.response.StatusSeguirResponse;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.service.SeguidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seguidor")
@RequiredArgsConstructor
public class SeguidorController {

    private final SeguidorService seguidorService;

    @PostMapping("/{seguidoId}")
    public void seguir(UsuarioAutenticado usuario, @PathVariable String seguidoId) {
        seguidorService.seguir(usuario.id(), seguidoId);
    }

    @DeleteMapping("/{seguidoId}")
    public void deixarDeSeguir(UsuarioAutenticado usuario, @PathVariable String seguidoId) {
        seguidorService.deixarDeSeguir(usuario.id(), seguidoId);
    }

    @GetMapping("/seguidores/{usuarioId}")
    public Page<UsuarioPublicoResponse> seguidores(@PathVariable String usuarioId, Pageable pageable) {
        return seguidorService.getSeguidores(usuarioId, pageable);
    }

    @GetMapping("/seguindo/{usuarioId}")
    public Page<UsuarioPublicoResponse> seguindo(@PathVariable String usuarioId, Pageable pageable) {
        return seguidorService.getSeguindo(usuarioId, pageable);
    }

    @GetMapping("/contadores/{usuarioId}")
    public ContadoresResponse contadores(@PathVariable String usuarioId) {
        return seguidorService.contadores(usuarioId);
    }

    @GetMapping("/status/{usuarioId}")
    public StatusSeguirResponse status(UsuarioAutenticado usuario, @PathVariable String usuarioId) {
        return seguidorService.status(usuario.id(), usuarioId);
    }
}

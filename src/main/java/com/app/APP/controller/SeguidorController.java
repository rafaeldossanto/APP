package com.app.APP.controller;

import com.app.APP.auth.UsuarioAutenticado;
import com.app.APP.model.dto.request.SeguirRequest;
import com.app.APP.model.dto.response.ContadoresResponse;
import com.app.APP.model.dto.response.StatusSeguirResponse;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.service.SeguidorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Alvo sempre pelo codigoUsuario (handle publico): no corpo em seguir/deixar de
 * seguir, em ?codigo= nos GET. Evita o '#' do codigo no path. O service resolve
 * o codigo para id.
 */
@RestController
@RequestMapping("/seguidor")
@RequiredArgsConstructor
public class SeguidorController {

    private final SeguidorService seguidorService;

    @PostMapping
    public void seguir(UsuarioAutenticado usuario, @RequestBody @Valid SeguirRequest request) {
        seguidorService.seguir(usuario.id(), request.seguidoCodigo());
    }

    @DeleteMapping
    public void deixarDeSeguir(UsuarioAutenticado usuario, @RequestBody @Valid SeguirRequest request) {
        seguidorService.deixarDeSeguir(usuario.id(), request.seguidoCodigo());
    }

    @GetMapping("/seguidores")
    public Page<UsuarioPublicoResponse> seguidores(@RequestParam String codigo, Pageable pageable) {
        return seguidorService.getSeguidores(codigo, pageable);
    }

    @GetMapping("/seguindo")
    public Page<UsuarioPublicoResponse> seguindo(@RequestParam String codigo, Pageable pageable) {
        return seguidorService.getSeguindo(codigo, pageable);
    }

    @GetMapping("/contadores")
    public ContadoresResponse contadores(@RequestParam String codigo) {
        return seguidorService.contadores(codigo);
    }

    @GetMapping("/status")
    public StatusSeguirResponse status(UsuarioAutenticado usuario, @RequestParam String codigo) {
        return seguidorService.status(usuario.id(), codigo);
    }
}

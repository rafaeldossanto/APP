package com.app.APP.controller;

import com.app.APP.auth.UsuarioAutenticado;
import com.app.APP.model.dto.request.RegiaoRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.dto.response.RegiaoResponse;
import com.app.APP.service.RegiaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regiao")
@RequiredArgsConstructor
public class RegiaoController {

    private final RegiaoService regiaoService;

    @PostMapping
    public RegiaoResponse criar(UsuarioAutenticado usuario, @RequestBody @Valid RegiaoRequest request) {
        return regiaoService.criar(usuario.id(), request);
    }

    @GetMapping
    public Page<RegiaoResponse> listarMinhas(UsuarioAutenticado usuario, Pageable pageable) {
        return regiaoService.listarMinhas(usuario.id(), pageable);
    }

    @GetMapping("/descobrir")
    public Page<RegiaoResponse> descobrir(UsuarioAutenticado usuario, Pageable pageable) {
        return regiaoService.descobrir(usuario.id(), pageable);
    }

    @GetMapping("/{id}")
    public RegiaoResponse getById(UsuarioAutenticado usuario, @PathVariable String id) {
        return regiaoService.getById(usuario.id(), id);
    }

    @GetMapping("/{id}/aventuras")
    public Page<AventuraResponse> getAventuras(UsuarioAutenticado usuario, @PathVariable String id, Pageable pageable) {
        return regiaoService.getAventuras(usuario.id(), id, pageable);
    }

    @PutMapping("/{id}")
    public RegiaoResponse atualizar(UsuarioAutenticado usuario, @PathVariable String id,
                                    @RequestBody @Valid RegiaoRequest request) {
        return regiaoService.atualizar(usuario.id(), id, request);
    }

    @DeleteMapping("/{id}")
    public void deletar(UsuarioAutenticado usuario, @PathVariable String id) {
        regiaoService.deletar(usuario.id(), id);
    }
}

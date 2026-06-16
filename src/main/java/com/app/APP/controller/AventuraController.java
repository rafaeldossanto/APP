package com.app.APP.controller;

import com.app.APP.auth.UsuarioAutenticado;
import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.dto.request.MoverRegiaoRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.enums.StatusAventura;
import com.app.APP.service.AventuraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aventura")
@RequiredArgsConstructor
public class AventuraController {

    private final AventuraService aventuraService;

    @PostMapping
    public AventuraResponse create(UsuarioAutenticado usuario, @RequestBody @Valid AventuraRequest request) {
        return aventuraService.create(usuario.id(), request);
    }

    @GetMapping("/{id}")
    public AventuraResponse getById(@PathVariable String id) {
        return aventuraService.getById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public Page<AventuraResponse> getByUsuario(@PathVariable String usuarioId, Pageable pageable) {
        return aventuraService.getByUsuario(usuarioId, pageable);
    }

    @PatchMapping("/{id}/status")
    public AventuraResponse atualizarStatus(UsuarioAutenticado usuario,
                                            @PathVariable String id,
                                            @RequestParam StatusAventura status) {
        return aventuraService.atualizarStatus(usuario.id(), id, status);
    }

    @PatchMapping("/{id}/regiao")
    public AventuraResponse moverRegiao(UsuarioAutenticado usuario, @PathVariable String id,
                                        @RequestBody MoverRegiaoRequest request) {
        return aventuraService.moverRegiao(usuario.id(), id, request.regiaoId());
    }

    @PostMapping("/{id}/participante")
    public void adicionarParticipante(@PathVariable String id,
                                      @RequestParam String usuarioId) {
        aventuraService.adicionarParticipante(id, usuarioId);
    }

    @DeleteMapping("/{id}")
    public void delete(UsuarioAutenticado usuario, @PathVariable String id) {
        aventuraService.delete(usuario.id(), id);
    }
}

package com.app.APP.controller;

import com.app.APP.auth.UsuarioAutenticado;
import com.app.APP.model.dto.request.AmizadeRequest;
import com.app.APP.model.dto.response.AmizadeResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.service.AmizadeService;
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
@RequestMapping("/amizade")
@RequiredArgsConstructor
public class AmizadeController {

    private final AmizadeService amizadeService;

    @PostMapping
    public AmizadeResponse solicitar(UsuarioAutenticado usuario, @RequestBody @Valid AmizadeRequest request) {
        return amizadeService.solicitar(usuario.id(), request);
    }

    @PatchMapping("/{id}/responder")
    public AmizadeResponse responder(UsuarioAutenticado usuario, @PathVariable String id,
                                     @RequestParam StatusAmizade status) {
        return amizadeService.responder(usuario.id(), id, status);
    }

    @DeleteMapping("/{id}/solicitacao")
    public void cancelarSolicitacao(UsuarioAutenticado usuario, @PathVariable String id) {
        amizadeService.cancelarSolicitacao(usuario.id(), id);
    }

    @DeleteMapping("/{id}")
    public void desfazerAmizade(UsuarioAutenticado usuario, @PathVariable String id) {
        amizadeService.desfazerAmizade(usuario.id(), id);
    }

    @PostMapping("/bloquear")
    public AmizadeResponse bloquear(UsuarioAutenticado usuario, @RequestBody @Valid AmizadeRequest request) {
        return amizadeService.bloquear(usuario.id(), request);
    }

    @DeleteMapping("/{id}/bloqueio")
    public void desbloquear(UsuarioAutenticado usuario, @PathVariable String id) {
        amizadeService.desbloquear(usuario.id(), id);
    }

    @GetMapping("/pendentes")
    public Page<AmizadeResponse> getPendentes(UsuarioAutenticado usuario, Pageable pageable) {
        return amizadeService.getPendentes(usuario.id(), pageable);
    }

    @GetMapping("/enviadas")
    public Page<AmizadeResponse> getEnviadas(UsuarioAutenticado usuario, Pageable pageable) {
        return amizadeService.getEnviadas(usuario.id(), pageable);
    }

    @GetMapping("/amigos")
    public Page<AmizadeResponse> getAmigos(UsuarioAutenticado usuario, Pageable pageable) {
        return amizadeService.getAmigos(usuario.id(), pageable);
    }

    /** Consulta de amizade entre dois usuarios — usada pelo servico de Localizacao (visibilidade AMIGOS). */
    @GetMapping("/sao-amigos")
    public boolean saoAmigos(@RequestParam String a, @RequestParam String b) {
        return amizadeService.saoAmigos(a, b);
    }
}

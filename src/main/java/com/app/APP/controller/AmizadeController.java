package com.app.APP.controller;

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
    public AmizadeResponse solicitar(@RequestBody @Valid AmizadeRequest request) {
        return amizadeService.solicitar(request);
    }

    @PatchMapping("/{id}/responder")
    public AmizadeResponse responder(@PathVariable String id,
                                     @RequestParam StatusAmizade status) {
        return amizadeService.responder(id, status);
    }

    @DeleteMapping("/{id}/solicitacao")
    public void cancelarSolicitacao(@PathVariable String id) {
        amizadeService.cancelarSolicitacao(id);
    }

    @DeleteMapping("/{id}")
    public void desfazerAmizade(@PathVariable String id) {
        amizadeService.desfazerAmizade(id);
    }

    @PostMapping("/bloquear")
    public AmizadeResponse bloquear(@RequestBody @Valid AmizadeRequest request) {
        return amizadeService.bloquear(request);
    }

    @DeleteMapping("/{id}/bloqueio")
    public void desbloquear(@PathVariable String id) {
        amizadeService.desbloquear(id);
    }

    @GetMapping("/pendentes/{usuarioId}")
    public Page<AmizadeResponse> getPendentes(@PathVariable String usuarioId, Pageable pageable) {
        return amizadeService.getPendentes(usuarioId, pageable);
    }

    @GetMapping("/enviadas/{usuarioId}")
    public Page<AmizadeResponse> getEnviadas(@PathVariable String usuarioId, Pageable pageable) {
        return amizadeService.getEnviadas(usuarioId, pageable);
    }

    @GetMapping("/amigos/{usuarioId}")
    public Page<AmizadeResponse> getAmigos(@PathVariable String usuarioId, Pageable pageable) {
        return amizadeService.getAmigos(usuarioId, pageable);
    }
}

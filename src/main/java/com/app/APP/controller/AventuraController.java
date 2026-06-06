package com.app.APP.controller;

import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.enums.StatusAventura;
import com.app.APP.service.AventuraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aventura")
@RequiredArgsConstructor
public class AventuraController {

    private final AventuraService aventuraService;

    @PostMapping
    public AventuraResponse create(@RequestBody @Valid AventuraRequest request) {
        return aventuraService.create(request);
    }

    @GetMapping("/{id}")
    public AventuraResponse getById(@PathVariable String id) {
        return aventuraService.getById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<AventuraResponse> getByUsuario(@PathVariable String usuarioId) {
        return aventuraService.getByUsuario(usuarioId);
    }

    @PatchMapping("/{id}/status")
    public AventuraResponse atualizarStatus(@PathVariable String id,
                                            @RequestParam StatusAventura status) {
        return aventuraService.atualizarStatus(id, status);
    }

    @PostMapping("/{id}/participante")
    public void adicionarParticipante(@PathVariable String id,
                                      @RequestParam String usuarioId) {
        aventuraService.adicionarParticipante(id, usuarioId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        aventuraService.delete(id);
    }
}

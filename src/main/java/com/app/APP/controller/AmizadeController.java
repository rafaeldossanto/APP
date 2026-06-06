package com.app.APP.controller;

import com.app.APP.model.dto.request.AmizadeRequest;
import com.app.APP.model.dto.response.AmizadeResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.service.AmizadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/pendentes/{usuarioId}")
    public List<AmizadeResponse> getPendentes(@PathVariable String usuarioId) {
        return amizadeService.getPendentes(usuarioId);
    }

    @GetMapping("/amigos/{usuarioId}")
    public List<AmizadeResponse> getAmigos(@PathVariable String usuarioId) {
        return amizadeService.getAmigos(usuarioId);
    }
}

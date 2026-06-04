package com.app.APP.controller;

import com.app.APP.model.dto.request.AmizadeRequest;
import com.app.APP.model.dto.response.AmizadeResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.service.AmizadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AmizadeResponse> solicitar(@RequestBody @Valid AmizadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(amizadeService.solicitar(request));
    }

    @PatchMapping("/{id}/responder")
    public ResponseEntity<AmizadeResponse> responder(@PathVariable String id,
                                                     @RequestParam StatusAmizade status) {
        return ResponseEntity.ok(amizadeService.responder(id, status));
    }

    @GetMapping("/pendentes/{usuarioId}")
    public ResponseEntity<List<AmizadeResponse>> getPendentes(@PathVariable String usuarioId) {
        return ResponseEntity.ok(amizadeService.getPendentes(usuarioId));
    }

    @GetMapping("/amigos/{usuarioId}")
    public ResponseEntity<List<AmizadeResponse>> getAmigos(@PathVariable String usuarioId) {
        return ResponseEntity.ok(amizadeService.getAmigos(usuarioId));
    }
}

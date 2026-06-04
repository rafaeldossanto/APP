package com.app.APP.controller;

import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.enums.StatusAventura;
import com.app.APP.service.AventuraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AventuraResponse> create(@RequestBody @Valid AventuraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aventuraService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventuraResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(aventuraService.getById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AventuraResponse>> getByUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(aventuraService.getByUsuario(usuarioId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AventuraResponse> atualizarStatus(@PathVariable String id,
                                                            @RequestParam StatusAventura status) {
        return ResponseEntity.ok(aventuraService.atualizarStatus(id, status));
    }

    @PostMapping("/{id}/participante")
    public ResponseEntity<Void> adicionarParticipante(@PathVariable String id,
                                                      @RequestParam String usuarioId) {
        aventuraService.adicionarParticipante(id, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        aventuraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

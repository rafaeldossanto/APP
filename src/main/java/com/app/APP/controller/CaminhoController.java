package com.app.APP.controller;

import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.dto.response.CaminhoResponse;
import com.app.APP.service.CaminhoService;
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
@RequestMapping("/caminho")
@RequiredArgsConstructor
public class CaminhoController {

    private final CaminhoService caminhoService;

    @PostMapping
    public ResponseEntity<CaminhoResponse> iniciar(@RequestBody @Valid CaminhoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caminhoService.iniciar(request));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<CaminhoResponse> finalizar(@PathVariable String id,
                                                     @RequestParam Double distanciaTotalKm) {
        return ResponseEntity.ok(caminhoService.finalizar(id, distanciaTotalKm));
    }

    @GetMapping("/aventura/{aventuraId}")
    public ResponseEntity<List<CaminhoResponse>> getByAventura(@PathVariable String aventuraId) {
        return ResponseEntity.ok(caminhoService.getByAventura(aventuraId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CaminhoResponse>> getByUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(caminhoService.getByUsuario(usuarioId));
    }
}

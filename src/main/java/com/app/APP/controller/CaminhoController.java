package com.app.APP.controller;

import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.dto.response.CaminhoResponse;
import com.app.APP.service.CaminhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caminho")
@RequiredArgsConstructor
public class CaminhoController {

    private final CaminhoService caminhoService;

    @PostMapping
    public CaminhoResponse iniciar(@RequestBody @Valid CaminhoRequest request) {
        return caminhoService.iniciar(request);
    }

    @PatchMapping("/{id}/finalizar")
    public CaminhoResponse finalizar(@PathVariable String id,
                                     @RequestParam Double distanciaTotalKm) {
        return caminhoService.finalizar(id, distanciaTotalKm);
    }

    @GetMapping("/aventura/{aventuraId}")
    public Page<CaminhoResponse> getByAventura(@PathVariable String aventuraId, Pageable pageable) {
        return caminhoService.getByAventura(aventuraId, pageable);
    }

    @GetMapping("/usuario/{usuarioId}")
    public Page<CaminhoResponse> getByUsuari
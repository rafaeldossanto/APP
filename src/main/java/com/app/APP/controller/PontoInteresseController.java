package com.app.APP.controller;

import com.app.APP.model.dto.request.EvidenciaRequest;
import com.app.APP.model.dto.request.PontoInteresseRequest;
import com.app.APP.model.dto.response.EvidenciaResponse;
import com.app.APP.model.dto.response.PontoInteresseResponse;
import com.app.APP.service.PontoInteresseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ponto-interesse")
@RequiredArgsConstructor
public class PontoInteresseController {

    private final PontoInteresseService pontoInteresseService;

    @PostMapping
    public PontoInteresseResponse create(@RequestBody @Valid PontoInteresseRequest request) {
        return pontoInteresseService.create(request);
    }

    @GetMapping("/{id}")
    public PontoInteresseResponse getById(@PathVariable String id) {
        return pontoInteresseService.getById(id);
    }

    @GetMapping("/caminho/{caminhoId}")
    public Page<PontoInteresseResponse> getByCaminho(@PathVariable String caminhoId, Pageable pageable) {
        return pontoInteresseService.getByCaminho(caminhoId, pageable);
    }

    @PostMapping("/evidencia")
    public EvidenciaResponse adicionarEvidencia(@RequestBody @Valid EvidenciaRequest request) {
        return pontoInteresseService.adicionarEvidencia(request);
    }
}

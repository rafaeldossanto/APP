package com.app.APP.controller;

import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.dto.response.MidiaResponse;
import com.app.APP.service.MidiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/midia")
@RequiredArgsConstructor
public class MidiaController {

    private final MidiaService midiaService;

    @PostMapping
    public MidiaResponse salvar(@RequestBody @Valid MidiaRequest request) {
        return midiaService.salvar(request);
    }

    @GetMapping("/aventura/{aventuraId}")
    public Page<MidiaResponse> getByAventura(@PathVariable String aventuraId, Pageable pageable) {
        return midiaService.getByAventura(aventuraId, pageable);
    }

    @GetMapping("/caminho/{caminhoId}")
    public Page<MidiaResponse> getByCaminho(@PathVariable String caminhoId, Pageable pageable) {
        return midiaService.getByCaminho(caminhoId, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        midiaService.delete(id);
    }
}

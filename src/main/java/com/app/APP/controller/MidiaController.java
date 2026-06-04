package com.app.APP.controller;

import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.dto.response.MidiaResponse;
import com.app.APP.service.MidiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<MidiaResponse> getByAventura(@PathVariable String aventuraId) {
        return midiaService.getByAventura(aventuraId);
    }

    @GetMapping("/caminho/{caminhoId}")
    public List<MidiaResponse> getByCaminho(@PathVariable String caminhoId) {
        return midiaService.getByCaminho(caminhoId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        midiaService.delete(id);
    }
}

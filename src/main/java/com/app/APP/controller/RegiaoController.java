package com.app.APP.controller;

import com.app.APP.model.dto.response.RegiaoResponse;
import com.app.APP.service.RegiaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regiao")
@RequiredArgsConstructor
public class RegiaoController {

    private final RegiaoService regiaoService;

    @GetMapping
    public List<RegiaoResponse> listar() {
        return regiaoService.listar();
    }
}

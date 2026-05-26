package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.dto.response.CaminhoResponse;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.CaminhoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaminhoService {

    //todo - organizar o esquema de caminho

    private final CaminhoRepository caminhoRepository;
    private final AventuraRepository aventuraRepository;

    public CaminhoResponse iniciar(CaminhoRequest request) {
        log.info("Iniciando caminho para usuario: {} na aventura: {}", request.usuarioId(), request.aventuraId());

        Aventura aventura = aventuraRepository.findById(request.aventuraId())
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));

        Caminho caminho = Caminho.builder()
                .id(UUID.randomUUID().toString())
                .aventura(aventura)
                .usuarioId(request.usuarioId())
                .cor(request.cor())
                .numero(request.numero())
                .iniciadoEm(LocalDateTime.now())
                .build();

        return toResponse(caminhoRepository.save(caminho));
    }

    // distancia total informada pelo servico de Localizacao ao finalizar
    public CaminhoResponse finalizar(String id, Double distanciaTotalKm) {
        log.info("Finalizando caminho: {}", id);
        Caminho caminho = findById(id);

        caminho.setFinalizadoEm(LocalDateTime.now());
        caminho.setDistanciaTotalKm(distanciaTotalKm);

        return toResponse(caminhoRepository.save(caminho));
    }

    public List<CaminhoResponse> getByAventura(String aventuraId) {
        return caminhoRepository.findByAventuraId(aventuraId)
                .stream().map(this::toResponse).toList();
    }

    public List<CaminhoResponse> getByUsuario(String usuarioId) {
        return caminhoRepository.findByUsuarioId(usuarioId)
                .stream().map(this::toResponse).toList();
    }

    private Caminho findById(String id) {
        return caminhoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caminho nao encontrado"));
    }

    private CaminhoResponse toResponse(Caminho c) {
        return CaminhoResponse.builder()
                .id(c.getId())
                .aventuraId(c.getAventura().getId())
                .usuarioId(c.getUsuarioId())
                .cor(c.getCor())
                .numero(c.getNumero())
                .iniciadoEm(c.getIniciadoEm())
                .finalizadoEm(c.getFinalizadoEm())
                .distanciaTotalKm(c.getDistanciaTotalKm())
                .build();
    }
}
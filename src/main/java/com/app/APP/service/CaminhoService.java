package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.mapper.CaminhoMapper;
import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.dto.response.CaminhoResponse;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.CaminhoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.app.APP.mapper.CaminhoMapper.toResponse;

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

        return toResponse(caminhoRepository.save(CaminhoMapper.toEntity(request, aventura)));
    }


    public CaminhoResponse finalizar(String id, Double distanciaTotalKm) {
        log.info("Finalizando caminho: {}", id);
        Caminho caminho = findById(id);

        caminho.setFinalizadoEm(LocalDateTime.now());
        caminho.setDistanciaTotalKm(distanciaTotalKm);

        return toResponse(caminhoRepository.save(caminho));
    }

    public Page<CaminhoResponse> getByAventura(String aventuraId, Pageable pageable) {
        return caminhoRepository.findByAventuraId(aventuraId, pageable)
                .map(CaminhoMapper::toResponse);
    }

    public Page<CaminhoResponse> getByUsuario(String usuarioId, Pageable pageable) {
        return caminhoRepository.findByUsuarioId(usuarioId, pageable)
                .map(CaminhoMapper::toResponse);
    }

    private Caminho findById(String id) {
        return caminhoRepository.findById(id)
           
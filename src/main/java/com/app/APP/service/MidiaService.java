package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.entity.Midia;
import com.app.APP.mapper.MidiaMapper;
import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.dto.response.MidiaResponse;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.CaminhoRepository;
import com.app.APP.repository.MidiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidiaService {

    private final MidiaRepository midiaRepository;
    private final AventuraRepository aventuraRepository;
    private final CaminhoRepository caminhoRepository;

    public MidiaResponse salvar(MidiaRequest request) {
        log.info("Salvando midia tipo: {} na aventura: {}", request.tipo(), request.aventuraId());

        Aventura aventura = aventuraRepository.findById(request.aventuraId())
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));

        Caminho caminho = null;
        if (nonNull(request.caminhoId())) {
            caminho = caminhoRepository.findById(request.caminhoId())
                    .orElseThrow(() -> new IllegalArgumentException("Caminho nao encontrado"));
        }

        Midia midia = midiaRepository.save(MidiaMapper.toEntity(request, aventura, caminho));

        return MidiaMapper.toResponse(midia);
    }

    public List<MidiaResponse> getByAventura(String aventuraId) {
        return midiaRepository.findByAventuraId(aventuraId)
                .stream().map(MidiaMapper::toResponse).toList();
    }

    public List<MidiaResponse> getByCaminho(String caminhoId) {
        return midiaRepository.findByCaminhoId(caminhoId)
                .stream().map(MidiaMapper::toResponse).toList();
    }

    public void delete(String id) {
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Midia nao encontrada"));
        midiaRepository.delete(midia);
        log.info("Midia {} deletada", id);
    }
}

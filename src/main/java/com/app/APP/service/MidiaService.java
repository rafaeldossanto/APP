package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.entity.Midia;
import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.dto.response.MidiaResponse;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.CaminhoRepository;
import com.app.APP.repository.MidiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        if (request.caminhoId() != null) {
            caminho = caminhoRepository.findById(request.caminhoId())
                    .orElseThrow(() -> new IllegalArgumentException("Caminho nao encontrado"));
        }

        Midia midia = Midia.builder()
                .id(UUID.randomUUID().toString())
                .aventura(aventura)
                .caminho(caminho)
                .usuarioId(request.usuarioId())
                .tipo(request.tipo())
                .url(request.url())
                .latCaptura(request.latCaptura())
                .lngCaptura(request.lngCaptura())
                .distanciaNaCapturaKm(request.distanciaNaCapturaKm())
                .percentualNoCaminho(request.percentualNoCaminho())
                .capturadaEm(LocalDateTime.now())
                .build();

        return toResponse(midiaRepository.save(midia));
    }

    public List<MidiaResponse> getByAventura(String aventuraId) {
        return midiaRepository.findByAventuraId(aventuraId)
                .stream().map(this::toResponse).toList();
    }

    public List<MidiaResponse> getByCaminho(String caminhoId) {
        return midiaRepository.findByCaminhoId(caminhoId)
                .stream().map(this::toResponse).toList();
    }

    public void delete(String id) {
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Midia nao encontrada"));
        midiaRepository.delete(midia);
        log.info("Midia {} deletada", id);
    }

    private MidiaResponse toResponse(Midia m) {
        return MidiaResponse.builder()
                .id(m.getId())
                .aventuraId(m.getAventura().getId())
                .caminhoId(m.getCaminho() != null ? m.getCaminho().getId() : null)
                .tipo(m.getTipo())
                .url(m.getUrl())
                .percentualNoCaminho(m.getPercentualNoCaminho())
                .distanciaNaCapturaKm(m.getDistanciaNaCapturaKm())
                .capturadaEm(m.getCapturadaEm())
                .build();
    }
}
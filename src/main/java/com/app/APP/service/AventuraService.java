package com.app.APP.service;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.ParticipanteAventura;
import com.app.APP.entity.Regiao;
import com.app.APP.mapper.AventuraMapper;
import com.app.APP.mapper.ParticipanteMapper;
import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.enums.StatusAventura;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.ParticipanteAventuraRepository;
import com.app.APP.repository.RegiaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.app.APP.mapper.AventuraMapper.toResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AventuraService {

    private final AventuraRepository aventuraRepository;
    private final ParticipanteAventuraRepository participanteRepository;
    private final RegiaoRepository regiaoRepository;

    public AventuraResponse create(AventuraRequest request) {
        log.info("Criando aventura para usuario: {}", request.usuarioId());

        Regiao regiao = regiaoRepository.findById(request.regiaoId())
                .orElseThrow(() -> new IllegalArgumentException("regiao nao encontrada"));

        var aventura = aventuraRepository.save(AventuraMapper.toEntity(request, regiao));

        participanteRepository.save(ParticipanteMapper.toEntity(aventura, request));

        log.info("Aventura criada com id: {}", aventura.getId());
        return toResponse(aventura);
    }

    public AventuraResponse getById(String id) {
        return toResponse(findById(id));
    }

    public List<AventuraResponse> getByUsuario(String usuarioId) {
        return aventuraRepository.findByUsuarioId(usuarioId)
                .stream().map(AventuraMapper::toResponse).toList();
    }

    public AventuraResponse atualizarStatus(String id, StatusAventura status) {
        log.info("Atualizando status da aventura {} para {}", id, status);
        Aventura aventura = findById(id);
        aventura.setStatus(status);
        aventura.setAtualizadoEm(LocalDateTime.now());
        return toResponse(aventuraRepository.save(aventura));
    }

    public void adicionarParticipante(String aventuraId, String usuarioId) {
        Aventura aventura = findById(aventuraId);

        if (participanteRepository.existsByAventuraIdAndUsuarioId(aventuraId, usuarioId)) {
            throw new IllegalArgumentException("Usuario ja participa dessa aventura");
        }

        participanteRepository.save(ParticipanteMapper.toEntity(aventura,  usuarioId));
        log.info("Usuario {} adicionado a aventura {}", usuarioId, aventuraId);
    }

    public void delete(String id) {
        Aventura aventura = findById(id);
        aventuraRepository.delete(aventura);
        log.info("Aventura {} deletada", id);
    }

    private Aventura findById(String id) {
        return aventuraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));
    }


}
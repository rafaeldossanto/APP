package com.app.APP.service;

import com.app.APP.entity.Aventura;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.app.APP.mapper.AventuraMapper.toResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AventuraService {

    private final AventuraRepository aventuraRepository;
    private final ParticipanteAventuraRepository participanteRepository;
    private final RegiaoRepository regiaoRepository;

    @Transactional
    public AventuraResponse create(String usuarioId, AventuraRequest request) {
        log.info("Criando aventura para usuario: {}", usuarioId);

        Regiao regiao = resolverRegiao(usuarioId, request.regiaoId());

        var aventura = aventuraRepository.save(AventuraMapper.toEntity(request, regiao, usuarioId));

        participanteRepository.save(ParticipanteMapper.toEntity(aventura, usuarioId));

        log.info("Aventura criada com id: {}", aventura.getId());
        return toResponse(aventura);
    }

    /** Move a aventura para uma pasta (regiao) ou tira dela (regiaoId nulo). */
    @Transactional
    public AventuraResponse moverRegiao(String usuarioId, String aventuraId, String regiaoId) {
        Aventura aventura = findDono(usuarioId, aventuraId);
        aventura.setRegiao(resolverRegiao(usuarioId, regiaoId));
        aventura.setAtualizadoEm(LocalDateTime.now());
        log.info("Aventura {} movida para regiao {}", aventuraId, regiaoId);
        return toResponse(aventuraRepository.save(aventura));
    }

    /** Regiao opcional: nula quando nao informada; se vier, precisa ser do usuario. */
    private Regiao resolverRegiao(String usuarioId, String regiaoId) {
        if (regiaoId == null || regiaoId.isBlank()) {
            return null;
        }
        Regiao regiao = regiaoRepository.findById(regiaoId)
                .orElseThrow(() -> new IllegalArgumentException("regiao nao encontrada"));
        if (!usuarioId.equals(regiao.getUsuarioId())) {
            throw new IllegalArgumentException("Voce nao e o dono desta regiao");
        }
        return regiao;
    }

    public AventuraResponse getById(String id) {
        return toResponse(findById(id));
    }

    public Page<AventuraResponse> getByUsuario(String usuarioId, Pageable pageable) {
        return aventuraRepository.findByUsuarioId(usuarioId, pageable)
                .map(AventuraMapper::toResponse);
    }

    public AventuraResponse atualizarStatus(String usuarioId, String id, StatusAventura status) {
        log.info("Atualizando status da aventura {} para {}", id, status);
        Aventura aventura = findDono(usuarioId, id);
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

    public void delete(String usuarioId, String id) {
        Aventura aventura = findDono(usuarioId, id);
        aventuraRepository.delete(aventura);
        log.info("Aventura {} deletada", id);
    }

    private Aventura findById(String id) {
        return aventuraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));
    }

    private Aventura findDono(String usuarioId, String id) {
        Aventura aventura = findById(id);
        if (!usuarioId.equals(aventura.getUsuarioId())) {
            throw new IllegalArgumentException("Voce nao e o dono desta aventura");
        }
        return aventura;
    }


}
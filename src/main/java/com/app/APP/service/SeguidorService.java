package com.app.APP.service;

import com.app.APP.entity.Seguidor;
import com.app.APP.model.dto.response.ContadoresResponse;
import com.app.APP.model.dto.response.StatusSeguirResponse;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.repository.AmizadesRepository;
import com.app.APP.repository.SeguidorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeguidorService {

    private final SeguidorRepository seguidorRepository;
    private final AmizadesRepository amizadesRepository;

    @Transactional
    public void seguir(String seguidorId, String seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("Voce nao pode seguir a si mesmo");
        }
        if (estaBloqueado(seguidorId, seguidoId)) {
            throw new IllegalArgumentException("Nao e possivel seguir: ha um bloqueio entre voces");
        }
        if (seguidorRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId)) {
            return;
        }
        seguidorRepository.save(Seguidor.builder()
                .id(UUID.randomUUID().toString())
                .seguidorId(seguidorId)
                .seguidoId(seguidoId)
                .criadoEm(LocalDateTime.now())
                .build());
        log.info("{} passou a seguir {}", seguidorId, seguidoId);
    }

    @Transactional
    public void deixarDeSeguir(String seguidorId, String seguidoId) {
        seguidorRepository.deleteBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
        log.info("{} deixou de seguir {}", seguidorId, seguidoId);
    }

    public Page<UsuarioPublicoResponse> getSeguidores(String usuarioId, Pageable pageable) {
        return seguidorRepository.findSeguidores(usuarioId, pageable);
    }

    public Page<UsuarioPublicoResponse> getSeguindo(String usuarioId, Pageable pageable) {
        return seguidorRepository.findSeguindo(usuarioId, pageable);
    }

    public ContadoresResponse contadores(String usuarioId) {
        return new ContadoresResponse(
                seguidorRepository.countBySeguidoId(usuarioId),
                seguidorRepository.countBySeguidorId(usuarioId));
    }

    /** Relacao de seguir entre o token (eu) e outro usuario. */
    public StatusSeguirResponse status(String eu, String outro) {
        boolean sigo = seguidorRepository.existsBySeguidorIdAndSeguidoId(eu, outro);
        boolean meSegue = seguidorRepository.existsBySeguidorIdAndSeguidoId(outro, eu);
        return new StatusSeguirResponse(sigo, meSegue, sigo && meSegue);
    }

    private boolean estaBloqueado(String a, String b) {
        return amizadesRepository.findRelacao(a, b)
                .filter(rel -> StatusAmizade.BLOQUEADA.equals(rel.getStatus()))
                .isPresent();
    }
}

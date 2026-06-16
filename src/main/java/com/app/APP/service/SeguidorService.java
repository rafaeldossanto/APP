package com.app.APP.service;

import com.app.APP.entity.Seguidor;
import com.app.APP.entity.Usuario;
import com.app.APP.model.dto.response.ContadoresResponse;
import com.app.APP.model.dto.response.StatusSeguirResponse;
import com.app.APP.model.dto.response.UsuarioPublicoResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.repository.AmizadesRepository;
import com.app.APP.repository.SeguidorRepository;
import com.app.APP.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Seguir e direcional e sem aceite. O alvo e informado pelo codigoUsuario
 * (handle publico, igual a amizade); o service resolve para o id interno.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SeguidorService {

    private final SeguidorRepository seguidorRepository;
    private final AmizadesRepository amizadesRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void seguir(String seguidorId, String seguidoCodigo) {
        String seguidoId = resolverId(seguidoCodigo);
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
    public void deixarDeSeguir(String seguidorId, String seguidoCodigo) {
        seguidorRepository.deleteBySeguidorIdAndSeguidoId(seguidorId, resolverId(seguidoCodigo));
        log.info("{} deixou de seguir {}", seguidorId, seguidoCodigo);
    }

    public Page<UsuarioPublicoResponse> getSeguidores(String usuarioCodigo, Pageable pageable) {
        return seguidorRepository.findSeguidores(resolverId(usuarioCodigo), pageable);
    }

    public Page<UsuarioPublicoResponse> getSeguindo(String usuarioCodigo, Pageable pageable) {
        return seguidorRepository.findSeguindo(resolverId(usuarioCodigo), pageable);
    }

    public ContadoresResponse contadores(String usuarioCodigo) {
        String usuarioId = resolverId(usuarioCodigo);
        return new ContadoresResponse(
                seguidorRepository.countBySeguidoId(usuarioId),
                seguidorRepository.countBySeguidorId(usuarioId));
    }

    /** Relacao de seguir entre o token (eu) e o usuario do codigo. */
    public StatusSeguirResponse status(String euId, String outroCodigo) {
        String outroId = resolverId(outroCodigo);
        boolean sigo = seguidorRepository.existsBySeguidorIdAndSeguidoId(euId, outroId);
        boolean meSegue = seguidorRepository.existsBySeguidorIdAndSeguidoId(outroId, euId);
        return new StatusSeguirResponse(sigo, meSegue, sigo && meSegue);
    }

    private String resolverId(String codigoUsuario) {
        return usuarioRepository.findByCodigoUsuario(codigoUsuario)
                .map(Usuario::getId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
    }

    private boolean estaBloqueado(String a, String b) {
        return amizadesRepository.findRelacao(a, b)
                .filter(rel -> StatusAmizade.BLOQUEADA.equals(rel.getStatus()))
                .isPresent();
    }
}

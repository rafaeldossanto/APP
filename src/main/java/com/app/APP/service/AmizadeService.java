package com.app.APP.service;

import com.app.APP.entity.Amizades;
import com.app.APP.mapper.AmizadeMapper;
import com.app.APP.model.dto.request.AmizadeRequest;
import com.app.APP.model.dto.response.AmizadeResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.repository.AmizadesRepository;
import com.app.APP.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.app.APP.mapper.AmizadeMapper.toResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class AmizadeService {

    private final AmizadesRepository amizadesRepository;
    private final UsuarioRepository usuarioRepository;

    public AmizadeResponse solicitar(AmizadeRequest request) {
        log.info("Solicitacao de amizade de {} para {}", request.solicitanteId(), request.receptorId());

        if (!usuarioRepository.existsById(request.receptorId())) {
            throw new IllegalArgumentException("Usuario receptor nao encontrado");
        }

        amizadesRepository.findRelacao(request.solicitanteId(), request.receptorId())
                .ifPresent(a -> { throw new IllegalArgumentException("Ja existe uma relacao entre esses usuarios"); });

        Amizades amizade = Amizades.builder()
                .id(UUID.randomUUID().toString())
                .solicitanteId(request.solicitanteId())
                .receptorId(request.receptorId())
                .solicitadoEm(LocalDateTime.now())
                .build();

        return toResponse(amizadesRepository.save(amizade));
    }

    public AmizadeResponse responder(String amizadeId, StatusAmizade status) {
        Amizades amizade = findById(amizadeId);

        if (!StatusAmizade.PENDENTE.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("Essa solicitacao ja foi respondida");
        }

        amizade.setStatus(status);
        amizade.setRespondidoEm(LocalDateTime.now());

        log.info("Amizade {} respondida com status: {}", amizadeId, status);
        return toResponse(amizadesRepository.save(amizade));
    }

    /** O solicitante desiste de uma solicitacao ainda pendente. */
    public void cancelarSolicitacao(String amizadeId) {
        Amizades amizade = findById(amizadeId);

        if (!StatusAmizade.PENDENTE.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("So e possivel cancelar uma solicitacao pendente");
        }

        amizadesRepository.delete(amizade);
        log.info("Solicitacao de amizade {} cancelada", amizadeId);
    }

    /** Desfaz uma amizade ja aceita (unfriend). */
    public void desfazerAmizade(String amizadeId) {
        Amizades amizade = findById(amizadeId);

        if (!StatusAmizade.ACEITA.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("So e possivel desfazer uma amizade aceita");
        }

        amizadesRepository.delete(amizade);
        log.info("Amizade {} desfeita", amizadeId);
    }

    /**
     * Bloqueia um usuario: reaproveita a relacao existente (se houver) ou cria uma
     * nova, marcando-a como BLOQUEADA e registrando quem efetuou o bloqueio.
     */
    public AmizadeResponse bloquear(AmizadeRequest request) {
        log.info("Usuario {} bloqueando {}", request.solicitanteId(), request.receptorId());

        if (!usuarioRepository.existsById(request.receptorId())) {
            throw new IllegalArgumentException("Usuario a bloquear nao encontrado");
        }

        Amizades amizade = amizadesRepository.findRelacao(request.solicitanteId(), request.receptorId())
                .orElseGet(() -> Amizades.builder()
                        .id(UUID.randomUUID().toString())
                        .solicitanteId(request.solicitanteId())
                        .receptorId(request.receptorId())
                        .solicitadoEm(LocalDateTime.now())
                        .build());

        amizade.setStatus(StatusAmizade.BLOQUEADA);
        amizade.setBloqueadoPor(request.solicitanteId());
        amizade.setRespondidoEm(LocalDateTime.now());

        return toResponse(amizadesRepository.save(amizade));
    }

    /** Remove um bloqueio, voltando ao estado sem relacao. */
    public void desbloquear(String amizadeId) {
        Amizades amizade = findById(amizadeId);

        if (!StatusAmizade.BLOQUEADA.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("Essa relacao nao esta bloqueada");
        }

        amizadesRepository.delete(amizade);
        log.info("Bloqueio da relacao {} removido", amizadeId);
    }

    /** Solicitacoes pendentes que CHEGARAM ao usuario (para ele responder). */
    public Page<AmizadeResponse> getPendentes(String usuarioId, Pageable pageable) {
        return amizadesRepository.findByReceptorIdAndStatus(usuarioId, StatusAmizade.PENDENTE, pageable)
                .map(AmizadeMapper::toResponse);
    }

    /** Solicitacoes pendentes que o usuario ENVIOU e aguardam resposta. */
    public Page<AmizadeResponse> getEnviadas(String usuarioId, Pageable pageable) {
        return amizadesRepository.findBySolicitanteIdAndStatus(usuarioId, StatusAmizade.PENDENTE, pageable)
                .map(AmizadeMapper::toResponse);
    }

    public Page<AmizadeResponse> getAmigos(String usuarioId, Pageable pageable) {
        return amizadesRepository.findByUsuarioIdAndStatus(usuarioId, StatusAmizade.ACEITA, pageable)
                .map(AmizadeMapper::toResponse);
    }

    private Amizades findById(String id) {
        return amizadesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Amizade nao encontrada"));
    }
}

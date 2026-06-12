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

import static com.app.APP.mapper.AmizadeMapper.toResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class AmizadeService {

    private final AmizadesRepository amizadesRepository;
    private final UsuarioRepository usuarioRepository;

    public AmizadeResponse solicitar(String solicitanteId, AmizadeRequest request) {
        log.info("Solicitacao de amizade de {} para {}", solicitanteId, request.receptorId());

        if (!usuarioRepository.existsById(request.receptorId())) {
            throw new IllegalArgumentException("Usuario receptor nao encontrado");
        }

        amizadesRepository.findRelacao(solicitanteId, request.receptorId())
                .ifPresent(a -> { throw new IllegalArgumentException("Ja existe uma relacao entre esses usuarios"); });

        return toResponse(amizadesRepository.save(AmizadeMapper.toEntity(solicitanteId, request)));
    }

    public AmizadeResponse responder(String usuarioId, String amizadeId, StatusAmizade status) {
        Amizades amizade = findById(amizadeId);

        if (!usuarioId.equals(amizade.getReceptorId())) {
            throw new IllegalArgumentException("Apenas o destinatario pode responder a solicitacao");
        }
        if (!StatusAmizade.PENDENTE.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("Essa solicitacao ja foi respondida");
        }

        amizade.setStatus(status);
        amizade.setRespondidoEm(LocalDateTime.now());

        log.info("Amizade {} respondida com status: {}", amizadeId, status);
        return toResponse(amizadesRepository.save(amizade));
    }

    /** O solicitante desiste de uma solicitacao ainda pendente. */
    public void cancelarSolicitacao(String usuarioId, String amizadeId) {
        Amizades amizade = findById(amizadeId);

        if (!usuarioId.equals(amizade.getSolicitanteId())) {
            throw new IllegalArgumentException("Apenas quem enviou pode cancelar a solicitacao");
        }
        if (!StatusAmizade.PENDENTE.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("So e possivel cancelar uma solicitacao pendente");
        }

        amizadesRepository.delete(amizade);
        log.info("Solicitacao de amizade {} cancelada", amizadeId);
    }

    /** Desfaz uma amizade ja aceita (unfriend) — qualquer um dos dois pode. */
    public void desfazerAmizade(String usuarioId, String amizadeId) {
        Amizades amizade = findById(amizadeId);
        validarParticipante(amizade, usuarioId);

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
    public AmizadeResponse bloquear(String bloqueadorId, AmizadeRequest request) {
        log.info("Usuario {} bloqueando {}", bloqueadorId, request.receptorId());

        if (!usuarioRepository.existsById(request.receptorId())) {
            throw new IllegalArgumentException("Usuario a bloquear nao encontrado");
        }

        Amizades amizade = amizadesRepository.findRelacao(bloqueadorId, request.receptorId())
                .orElseGet(() -> AmizadeMapper.toEntity(bloqueadorId, request));

        amizade.setStatus(StatusAmizade.BLOQUEADA);
        amizade.setBloqueadoPor(bloqueadorId);
        amizade.setRespondidoEm(LocalDateTime.now());

        return toResponse(amizadesRepository.save(amizade));
    }

    /** Remove um bloqueio — apenas quem bloqueou pode. */
    public void desbloquear(String usuarioId, String amizadeId) {
        Amizades amizade = findById(amizadeId);

        if (!StatusAmizade.BLOQUEADA.equals(amizade.getStatus())) {
            throw new IllegalArgumentException("Essa relacao nao esta bloqueada");
        }
        if (!usuarioId.equals(amizade.getBloqueadoPor())) {
            throw new IllegalArgumentException("Apenas quem bloqueou pode desbloquear");
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

    /** Indica se os dois usuarios sao amigos (relacao ACEITA) — usado pelo loc. */
    public boolean saoAmigos(String usuarioA, String usuarioB) {
        return amizadesRepository.findRelacao(usuarioA, usuarioB)
                .filter(a -> StatusAmizade.ACEITA.equals(a.getStatus()))
                .isPresent();
    }

    private Amizades findById(String id) {
        return amizadesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Amizade nao encontrada"));
    }

    private void validarParticipante(Amizades amizade, String usuarioId) {
        boolean participa = usuarioId.equals(amizade.getSolicitanteId()) || usuarioId.equals(amizade.getReceptorId());
        if (!participa) {
            throw new IllegalArgumentException("Voce nao participa dessa relacao");
        }
    }
}

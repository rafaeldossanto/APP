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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public List<AmizadeResponse> getPendentes(String usuarioId) {
        return amizadesRepository.findByUsuarioIdAndStatus(usuarioId, StatusAmizade.PENDENTE)
                .stream().map(AmizadeMapper::toResponse).toList();
    }

    public List<AmizadeResponse> getAmigos(String usuarioId) {
        return amizadesRepository.findByUsuarioIdAndStatus(usuarioId, StatusAmizade.ACEITA)
                .stream().map(AmizadeMapper::toResponse).toList();
    }

    private Amizades findById(String id) {
        return amizadesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Amizade nao encontrada"));
    }
}
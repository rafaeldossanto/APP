package com.app.APP.service;

import com.app.APP.entity.Regiao;
import com.app.APP.mapper.AventuraMapper;
import com.app.APP.mapper.RegiaoMapper;
import com.app.APP.model.dto.request.RegiaoRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import com.app.APP.model.dto.response.RegiaoResponse;
import com.app.APP.model.enums.StatusAmizade;
import com.app.APP.repository.AmizadesRepository;
import com.app.APP.repository.AventuraRepository;
import com.app.APP.repository.RegiaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;
    private final AmizadesRepository amizadesRepository;
    private final AventuraRepository aventuraRepository;

    @Transactional
    public RegiaoResponse criar(String usuarioId, RegiaoRequest request) {
        log.info("Criando regiao '{}' para usuario {}", request.nome(), usuarioId);
        return RegiaoMapper.toResponse(regiaoRepository.save(RegiaoMapper.toEntity(request, usuarioId)));
    }

    public Page<RegiaoResponse> listarMinhas(String usuarioId, Pageable pageable) {
        return regiaoRepository.findByUsuarioId(usuarioId, pageable).map(RegiaoMapper::toResponse);
    }

    public RegiaoResponse getById(String usuarioId, String id) {
        Regiao regiao = buscar(id);
        validarAcesso(regiao, usuarioId);
        return RegiaoMapper.toResponse(regiao);
    }

    @Transactional
    public RegiaoResponse atualizar(String usuarioId, String id, RegiaoRequest request) {
        Regiao regiao = buscarDono(id, usuarioId);
        RegiaoMapper.aplicar(regiao, request);
        return RegiaoMapper.toResponse(regiaoRepository.save(regiao));
    }

    /** Apaga a pasta; as aventuras nao sao apagadas — apenas desvinculadas. */
    @Transactional
    public void deletar(String usuarioId, String id) {
        Regiao regiao = buscarDono(id, usuarioId);
        aventuraRepository.desvincularRegiao(id);
        regiaoRepository.delete(regiao);
        log.info("Regiao {} apagada; aventuras desvinculadas", id);
    }

    /** Pastas visiveis ao usuario que nao sao dele (PUBLICA de todos + AMIGOS de amigos). */
    public Page<RegiaoResponse> descobrir(String usuarioId, Pageable pageable) {
        List<String> amigos = amizadesRepository.findAmigoIds(usuarioId, StatusAmizade.ACEITA);
        List<String> filtro = amigos.isEmpty() ? List.of("__sem_amigos__") : amigos;
        return regiaoRepository.findDescobriveis(usuarioId, filtro, pageable).map(RegiaoMapper::toResponse);
    }

    /** Aventuras de uma pasta, ja filtradas pela visibilidade de cada aventura. */
    public Page<AventuraResponse> getAventuras(String usuarioId, String regiaoId, Pageable pageable) {
        Regiao regiao = buscar(regiaoId);
        validarAcesso(regiao, usuarioId);
        return aventuraRepository.findVisiveisNaRegiao(regiaoId, usuarioId, pageable).map(AventuraMapper::toResponse);
    }

    /** Acesso de leitura a pasta: dono sempre; PUBLICA todos; AMIGOS so amigos do dono; PRIVADA so dono. */
    private void validarAcesso(Regiao regiao, String usuarioId) {
        if (usuarioId.equals(regiao.getUsuarioId())) {
            return;
        }
        boolean permitido = switch (regiao.getVisibilidade()) {
            case PUBLICA -> true;
            case AMIGOS -> amizadesRepository.findRelacao(usuarioId, regiao.getUsuarioId())
                    .filter(a -> StatusAmizade.ACEITA.equals(a.getStatus()))
                    .isPresent();
            case PRIVADA -> false;
        };
        if (!permitido) {
            throw new IllegalArgumentException("Regiao nao encontrada ou sem acesso");
        }
    }

    private Regiao buscar(String id) {
        return regiaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Regiao nao encontrada"));
    }

    private Regiao buscarDono(String id, String usuarioId) {
        Regiao regiao = buscar(id);
        if (!usuarioId.equals(regiao.getUsuarioId())) {
            throw new IllegalArgumentException("Voce nao e o dono desta regiao");
        }
        return regiao;
    }
}

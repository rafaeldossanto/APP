package com.app.APP.service;

import com.app.APP.entity.Caminho;
import com.app.APP.entity.Evidencia;
import com.app.APP.entity.PontoInteresse;
import com.app.APP.mapper.PontoInteresseMapper;
import com.app.APP.model.dto.request.EvidenciaRequest;
import com.app.APP.model.dto.request.PontoInteresseRequest;
import com.app.APP.model.dto.response.EvidenciaResponse;
import com.app.APP.model.dto.response.PontoInteresseResponse;
import com.app.APP.repository.CaminhoRepository;
import com.app.APP.repository.EvidenciaRepository;
import com.app.APP.repository.PontoInteresseRepository;
import com.app.APP.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class PontoInteresseService {

    private final PontoInteresseRepository pontoRepository;
    private final EvidenciaRepository evidenciaRepository;
    private final CaminhoRepository caminhoRepository;

    private static final double DISTANCIA_MAXIMA_METROS = 50.0;

    public PontoInteresseResponse create(PontoInteresseRequest request) {
        log.info("Criando ponto de interesse tipo: {}", request.tipo());

        Caminho caminho = caminhoRepository.findById(request.caminhoId())
                .orElseThrow(() -> new IllegalArgumentException("Caminho nao encontrado"));

        PontoInteresse ponto = pontoRepository.save(PontoInteresseMapper.toEntity(request, caminho));

        return PontoInteresseMapper.toResponse(ponto, 1);
    }

    public EvidenciaResponse adicionarEvidencia(EvidenciaRequest request) {
        PontoInteresse ponto = pontoRepository.findById(request.pontoId())
                .orElseThrow(() -> new IllegalArgumentException("Ponto nao encontrado"));

        double distancia = GeoUtils.distanciaMetros(
                ponto.getLatitude(), ponto.getLongitude(),
                request.latCaptura(), request.lngCaptura()
        );

        if (distancia > DISTANCIA_MAXIMA_METROS) {
            log.warn("Evidencia rejeitada — distancia do ponto: {}m (limite: {}m)", distancia, DISTANCIA_MAXIMA_METROS);
            throw new IllegalArgumentException("Voce esta muito longe do ponto para adicionar evidencia. Distancia: " + (int) distancia + "m");
        }

        Evidencia evidencia = evidenciaRepository.save(
                PontoInteresseMapper.toEvidenciaEntity(request, ponto, distancia)
        );
        log.info("Evidencia validada e salva para ponto: {}", ponto.getId());

        return PontoInteresseMapper.toEvidenciaResponse(evidencia);
    }

    public PontoInteresseResponse getById(String id) {
        PontoInteresse ponto = pontoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ponto nao encontrado"));
        return PontoInteresseMapper.toResponse(ponto, calcularNivel(ponto));
    }

    public Page<PontoInteresseResponse> getByCaminho(String caminhoId, Pageable pageable) {
        Page<PontoInteresse> pagina = pontoRepository.findByCaminhoId(caminhoId, pageable);

        Map<String, Long> validadosPorPonto = contarValidadosEmLote(pagina.getContent());

        return pagina.map(p -> PontoInteresseMapper.toResponse(
                p, calcularNivel(p, validadosPorPonto.getOrDefault(p.getId(), 0L))));
    }

    private Map<String, Long> contarValidadosEmLote(List<PontoInteresse> pontos) {
        if (pontos.isEmpty()) {
            return Map.of();
        }
        List<String> ids = pontos.stream().map(PontoInteresse::getId).toList();
        Map<String, Long> resultado = new HashMap<>();
        for (Object[] linha : evidenciaRepository.countUsuariosValidadosPorPontos(ids)) {
            resultado.put((String) linha[0], (Long) linha[1]);
        }
        return resultado;
    }

    private int calcularNivel(PontoInteresse ponto) {
        return calcularNivel(ponto, evidenciaRepository.countUsuariosValidadosByPontoId(ponto.getId()));
    }

    private int calcularNivel(PontoInteresse ponto, long usuariosDistintos) {
        boolean temDescricao = nonNull(ponto.getDescricao()) && !ponto.getDescricao().isBlank();

        if (usuariosDistintos >= 3 && temDescricao) return 5;
        if (usuariosDistintos >= 2 && temDescricao) return 4;
        if (usuariosDistintos >= 1 && temDescricao) return 3;
        if (temDescricao) return 2;
        return 1;
    }

}

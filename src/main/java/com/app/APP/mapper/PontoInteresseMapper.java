package com.app.APP.mapper;

import com.app.APP.entity.Evidencia;
import com.app.APP.entity.PontoInteresse;
import com.app.APP.model.dto.request.PontoInteresseRequest;
import com.app.APP.model.dto.response.EvidenciaResponse;
import com.app.APP.model.dto.response.PontoInteresseResponse;
import com.app.APP.entity.Caminho;
import com.app.APP.model.dto.request.EvidenciaRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class PontoInteresseMapper {

    public static PontoInteresse toEntity(PontoInteresseRequest request, Caminho caminho, String usuarioId) {
        return PontoInteresse.builder()
                .id(UUID.randomUUID().toString())
                .caminho(caminho)
                .usuarioId(usuarioId)
                .tipo(request.tipo())
                .nome(request.nome())
                .descricao(request.descricao())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .criadoEm(LocalDateTime.now())
                .build();
    }

    public static Evidencia toEvidenciaEntity(EvidenciaRequest request, PontoInteresse ponto, double distancia, String usuarioId) {
        return Evidencia.builder()
                .id(UUID.randomUUID().toString())
                .ponto(ponto)
                .usuarioId(usuarioId)
                .fotoUrl(request.fotoUrl())
                .tipoEvidencia(request.tipoEvidencia())
                .latCaptura(request.latCaptura())
                .lngCaptura(request.lngCaptura())
                .distanciaDopontoM(distancia)
                .capturadaNoApp(true)
                .validada(true)
                .criadoEm(LocalDateTime.now())
                .build();
    }

    public static PontoInteresseResponse toResponse(PontoInteresse ponto, int nivelConfianca) {
        return PontoInteresseResponse.builder()
                .id(ponto.getId())
                .caminhoId(ponto.getCaminho().getId())
                .usuarioId(ponto.getUsuarioId())
                .tipo(ponto.getTipo())
                .nome(ponto.getNome())
                .descricao(ponto.getDescricao())
                .latitude(ponto.getLatitude())
                .longitude(ponto.getLongitude())
                .nivelConfianca(nivelConfianca)
                .criadoEm(ponto.getCriadoEm())
                .build();
    }

    public static EvidenciaResponse toEvidenciaResponse(Evidencia evidencia) {
        return EvidenciaResponse.builder()
                .id(evidencia.getId())
                .pontoId(evidencia.getPonto().getId())
                .usuarioId(evidencia.getUsuarioId())
                .fotoUrl(evidencia.getFotoUrl())
                .tipoEvidencia(evidencia.getTipoEvidencia())
                .validada(evidencia.getValidada())
                .criadoEm(evidencia.getCriadoEm())
                .build();
    }
}

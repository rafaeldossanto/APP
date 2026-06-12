package com.app.APP.mapper;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.entity.Midia;
import com.app.APP.model.dto.request.MidiaRequest;
import com.app.APP.model.dto.response.MidiaResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.nonNull;

@UtilityClass
public class MidiaMapper {

    public static Midia toEntity(MidiaRequest request, Aventura aventura, Caminho caminho, String usuarioId) {
        return Midia.builder()
                .id(UUID.randomUUID().toString())
                .aventura(aventura)
                .caminho(caminho)
                .usuarioId(usuarioId)
                .tipo(request.tipo())
                .url(request.url())
                .latCaptura(request.latCaptura())
                .lngCaptura(request.lngCaptura())
                .distanciaNaCapturaKm(request.distanciaNaCapturaKm())
                .percentualNoCaminho(request.percentualNoCaminho())
                .capturadaEm(LocalDateTime.now())
                .build();
    }

    public static MidiaResponse toResponse(Midia midia) {
        return MidiaResponse.builder()
                .id(midia.getId())
                .aventuraId(midia.getAventura().getId())
                .caminhoId(nonNull(midia.getCaminho()) ? midia.getCaminho().getId() : null)
                .tipo(midia.getTipo())
                .url(midia.getUrl())
                .percentualNoCaminho(midia.getPercentualNoCaminho())
                .distanciaNaCapturaKm(midia.getDistanciaNaCapturaKm())
                .capturadaEm(midia.getCapturadaEm())
                .build();
    }
}

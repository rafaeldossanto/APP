package com.app.APP.mapper;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Caminho;
import com.app.APP.model.dto.request.CaminhoRequest;
import com.app.APP.model.dto.response.CaminhoResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CaminhoMapper {

    public static Caminho toEntity(CaminhoRequest request, Aventura aventura, int numero) {
        return Caminho.builder()
                .aventura(aventura)
                .usuarioId(request.usuarioId())
                .cor(request.cor())
                .numero(numero)
                .iniciadoEm(LocalDateTime.now())
                .build();
    }

    public static CaminhoResponse toResponse(Caminho caminho) {
        return CaminhoResponse.builder()
                .id(caminho.getId())
                .aventuraId(caminho.getAventura().getId())
                .usuarioId(caminho.getUsuarioId())
                .cor(caminho.getCor())
                .numero(caminho.getNumero())
                .iniciadoEm(caminho.getIniciadoEm())
                .finalizadoEm(caminho.getFinalizadoEm())
                .distanciaTotalKm(caminho.getDistanciaTotalKm())
                .build();
    }
}

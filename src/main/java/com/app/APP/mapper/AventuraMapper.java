package com.app.APP.mapper;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.Regiao;
import com.app.APP.model.dto.request.AventuraRequest;
import com.app.APP.model.dto.response.AventuraResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@UtilityClass
public class AventuraMapper {

    public static Aventura toEntity(AventuraRequest request, Regiao regiao) {
        return Aventura.builder()
                .usuarioId(request.usuarioId())
                .regiao(regiao)
                .destino(request.destino())
                .visibilidade(request.visibilidade())
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();
    }

    public static AventuraResponse toResponse(Aventura aventura) {
        return AventuraResponse.builder()
                .id(aventura.getId())
                .usuarioId(aventura.getUsuarioId())
                .regiaoId(nonNull(aventura.getRegiao()) ? aventura.getRegiao().getId() : null)
                .destino(aventura.getDestino())
                .status(aventura.getStatus())
                .visibilidade(aventura.getVisibilidade())
                .criadoEm(aventura.getCriadoEm())
                .build();
    }
}
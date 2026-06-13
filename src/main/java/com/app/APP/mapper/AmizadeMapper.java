package com.app.APP.mapper;

import com.app.APP.entity.Amizades;
import com.app.APP.model.dto.response.AmizadeResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class AmizadeMapper {

    public static Amizades toEntity(String solicitanteId, String receptorId) {
        return Amizades.builder()
                .id(UUID.randomUUID().toString())
                .solicitanteId(solicitanteId)
                .receptorId(receptorId)
                .solicitadoEm(LocalDateTime.now())
                .build();
    }

    public static AmizadeResponse toResponse(Amizades a) {
        return AmizadeResponse.builder()
                .id(a.getId())
                .solicitanteId(a.getSolicitanteId())
                .receptorId(a.getReceptorId())
                .status(a.getStatus())
                .bloqueadoPor(a.getBloqueadoPor())
                .solicitadoEm(a.getSolicitadoEm())
                .respondidoEm(a.getRespondidoEm())
                .build();
    }
}

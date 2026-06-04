package com.app.APP.mapper;

import com.app.APP.entity.Amizades;
import com.app.APP.model.dto.response.AmizadeResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AmizadeMapper {

    public static AmizadeResponse toResponse(Amizades a) {
        return AmizadeResponse.builder()
                .id(a.getId())
                .solicitanteId(a.getSolicitanteId())
                .receptorId(a.getReceptorId())
                .status(a.getStatus())
                .solicitadoEm(a.getSolicitadoEm())
                .respondidoEm(a.getRespondidoEm())
                .build();
    }
}

package com.app.APP.mapper;

import com.app.APP.entity.Regiao;
import com.app.APP.model.dto.response.RegiaoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegiaoMapper {

    public static RegiaoResponse toResponse(Regiao regiao) {
        return RegiaoResponse.builder()
                .id(regiao.getId())
                .nome(regiao.getNome())
                .descricao(regiao.getDescricao())
                .latMin(regiao.getLatMin())
                .latMax(regiao.getLatMax())
                .lngMin(regiao.getLngMin())
                .lngMax(regiao.getLngMax())
                .build();
    }
}

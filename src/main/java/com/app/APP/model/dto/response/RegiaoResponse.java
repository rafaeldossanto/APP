package com.app.APP.model.dto.response;

import lombok.Builder;

@Builder
public record RegiaoResponse(
        String id,
        String nome,
        String descricao,
        Double latMin,
        Double latMax,
        Double lngMin,
        Double lngMax
) {}

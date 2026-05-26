package com.app.APP.model.dto.request;

import com.app.APP.model.enums.TipoEvidencia;

public record EvidenciaRequest(
        String pontoId,
        String usuarioId,
        String fotoUrl,
        TipoEvidencia tipoEvidencia,
        Double latCaptura,
        Double lngCaptura
) {}
package com.app.APP.model.dto.response;

import com.app.APP.model.dto.request.CidadeDTO;
import com.app.APP.model.enums.VisibilidadeRegiao;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RegiaoResponse(
        String id,
        String usuarioId,
        String nome,
        String descricao,
        VisibilidadeRegiao visibilidade,
        List<CidadeDTO> cidades,
        LocalDateTime criadoEm
) {}

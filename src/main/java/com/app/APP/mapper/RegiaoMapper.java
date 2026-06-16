package com.app.APP.mapper;

import com.app.APP.entity.Cidade;
import com.app.APP.entity.Regiao;
import com.app.APP.model.dto.request.CidadeDTO;
import com.app.APP.model.dto.request.RegiaoRequest;
import com.app.APP.model.dto.response.RegiaoResponse;
import com.app.APP.model.enums.VisibilidadeRegiao;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@UtilityClass
public class RegiaoMapper {

    public static Regiao toEntity(RegiaoRequest request, String usuarioId) {
        LocalDateTime agora = LocalDateTime.now();
        return Regiao.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(usuarioId)
                .nome(request.nome())
                .descricao(request.descricao())
                .visibilidade(visibilidadeOuPadrao(request))
                .cidades(toCidades(request.cidades()))
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();
    }

    /** Aplica os campos editaveis numa regiao existente (PUT). */
    public static void aplicar(Regiao regiao, RegiaoRequest request) {
        regiao.setNome(request.nome());
        regiao.setDescricao(request.descricao());
        regiao.setVisibilidade(visibilidadeOuPadrao(request));
        regiao.setCidades(toCidades(request.cidades()));
        regiao.setAtualizadoEm(LocalDateTime.now());
    }

    public static RegiaoResponse toResponse(Regiao regiao) {
        return RegiaoResponse.builder()
                .id(regiao.getId())
                .usuarioId(regiao.getUsuarioId())
                .nome(regiao.getNome())
                .descricao(regiao.getDescricao())
                .visibilidade(regiao.getVisibilidade())
                .cidades(regiao.getCidades().stream()
                        .map(c -> new CidadeDTO(c.getNome(), c.getLatitude(), c.getLongitude(), c.getAltitude()))
                        .toList())
                .criadoEm(regiao.getCriadoEm())
                .build();
    }

    private static VisibilidadeRegiao visibilidadeOuPadrao(RegiaoRequest request) {
        return isNull(request.visibilidade()) ? VisibilidadeRegiao.PRIVADA : request.visibilidade();
    }

    private static List<Cidade> toCidades(List<CidadeDTO> cidades) {
        if (isNull(cidades)) {
            return new ArrayList<>();
        }
        return cidades.stream()
                .map(c -> Cidade.builder()
                        .nome(c.nome())
                        .latitude(c.latitude())
                        .longitude(c.longitude())
                        .altitude(c.altitude())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

package com.app.APP.mapper;

import com.app.APP.entity.Aventura;
import com.app.APP.entity.ParticipanteAventura;
import com.app.APP.model.dto.request.AventuraRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class ParticipanteMapper {

    public static ParticipanteAventura toEntity(Aventura aventura, AventuraRequest request){
        return ParticipanteAventura.builder()
                .id(UUID.randomUUID().toString())
                .aventura(aventura)
                .usuarioId(request.usuarioId())
                .entradoEm(LocalDateTime.now())
                .build();
    }

    public static ParticipanteAventura toEntity(Aventura aventura, String participante){
        return ParticipanteAventura.builder()
                .id(UUID.randomUUID().toString())
                .aventura(aventura)
                .usuarioId(participante)
                .entradoEm(LocalDateTime.now())
                .build();
    }
}

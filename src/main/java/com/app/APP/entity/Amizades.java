package com.app.APP.entity;

import com.app.APP.model.enums.StatusAmizade;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amizades {

    private @Id String id;

    private String solicitanteId;
    private String receptorId;

    @Builder.Default
    private StatusAmizade status = StatusAmizade.PENDENTE;

    private LocalDateTime solicitadoEm;
}

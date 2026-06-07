package com.app.APP.entity;

import com.app.APP.model.enums.TipoEvidencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias", indexes = {
        @Index(name = "idx_evidencia_ponto", columnList = "ponto_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidencia {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id", nullable = false)
    private PontoInteresse ponto;

    @Column(nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    private TipoEvidencia tipoEvidencia;

    private Double latCaptura;
    private Double lngCaptura;
    private Double distanciaDopontoM;

    @Column(nullable = false)
    @Builder.Def
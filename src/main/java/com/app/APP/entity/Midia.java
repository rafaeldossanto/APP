package com.app.APP.entity;

import com.app.APP.model.enums.TipoMidia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
@Table(name = "midias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Midia {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "aventura_id", nullable = false)
    private Aventura aventura;

    // nullable — se nulo, midia é avulsa na aventura
    @ManyToOne
    @JoinColumn(name = "caminho_id")
    private Caminho caminho;

    @Column(nullable = false)
    private String usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMidia tipo;

    @Column(nullable = false)
    private String url;

    private Double latCaptura;
    private Double lngCaptura;
    private Double distanciaNaCapturaKm;
    private Double percentualNoCaminho; // ex: 0.20 = 20%

    private LocalDateTime capturadaEm;
}
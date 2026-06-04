package com.app.APP.entity;

import com.app.APP.model.enums.Cores;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import static com.app.APP.model.enums.Cores.ROXO;

@Entity
@Table(name = "caminhos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caminho {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "aventura_id", nullable = false)
    private Aventura aventura;

    @Column(nullable = false)
    private String usuarioId;

    @Builder.Default
    private Cores cor = ROXO;
    private Integer numero;

    private LocalDateTime iniciadoEm;
    private LocalDateTime finalizadoEm;
    private Double distanciaTotalKm;
}
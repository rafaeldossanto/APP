package com.app.APP.entity;

import com.app.APP.model.enums.Cores;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import static com.app.APP.model.enums.Cores.ROXO;

@Entity
@Table(name = "caminhos", indexes = {
        @Index(name = "idx_caminho_aventura", columnList = "aventura_id"),
        @Index(name = "idx_caminho_usuario", columnList = "usuarioId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caminho {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
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
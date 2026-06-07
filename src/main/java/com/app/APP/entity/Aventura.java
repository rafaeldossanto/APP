package com.app.APP.entity;

import com.app.APP.model.enums.StatusAventura;
import com.app.APP.model.enums.VisibilidadeAventura;
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
@Table(name = "aventuras", indexes = {
        @Index(name = "idx_aventura_usuario", columnList = "usuarioId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aventura {

    @Id
    private String id;

    @Column(nullable = false)
    private String usuarioId; // criador

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regiao_id")
    private Regiao regiao;

    @Column(nullable = false)
    private String destino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusAventura status = StatusAventura.PLANEJADA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VisibilidadeAventura visibilidade = VisibilidadeAventura.PRIVADA;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
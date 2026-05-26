package com.app.APP.entity;

import com.app.APP.model.enums.TipoPonto;
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
@Table(name = "pontos_interesse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PontoInteresse {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "caminho_id", nullable = false)
    private Caminho caminho;

    @Column(nullable = false)
    private String usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPonto tipo;

    private String nome;
    private String descricao;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private LocalDateTime criadoEm;
}
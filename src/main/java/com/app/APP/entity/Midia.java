package com.app.APP.entity;

import com.app.APP.model.enums.TipoMidia;
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
@Table(name = "midias", indexes = {
        @Index(name = "idx_midia_aventura", columnList = "aventura_id"),
        @Index(name = "idx_midia_caminho", columnList = "caminho_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Midia {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aventura_id", nullable = false)
    private Aventura aventura;

    // nullable — se nulo, midia é avulsa na aventura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminho_id")
    private Caminho caminho;

    @Column(nullable = false)
    private String usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ti
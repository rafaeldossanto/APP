package com.app.APP.entity;

import com.app.APP.model.enums.RegionVisibility;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Regiao = "pasta" de aventuras do usuario. Tem dono, visibilidade propria e
 * uma lista de cidades que a compoem.
 */
@Entity
@Table(name = "regioes", indexes = {
        @Index(name = "idx_regiao_usuario", columnList = "usuario_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    private String id;

    @Column(name = "usuario_id", nullable = false)
    private String userId;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "descricao")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibilidade", nullable = false)
    @Builder.Default
    private RegionVisibility visibility = RegionVisibility.PRIVADA;

    @ElementCollection
    @CollectionTable(name = "regiao_cidades", joinColumns = @JoinColumn(name = "regiao_id"))
    @Builder.Default
    private List<City> cities = new ArrayList<>();

    @Column(name = "criado_em")
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em")
    private LocalDateTime updatedAt;
}

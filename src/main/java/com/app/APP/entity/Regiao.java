package com.app.APP.entity;

import com.app.APP.model.enums.VisibilidadeRegiao;
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
        @Index(name = "idx_regiao_usuario", columnList = "usuarioId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regiao {

    @Id
    private String id;

    @Column(nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VisibilidadeRegiao visibilidade = VisibilidadeRegiao.PRIVADA;

    @ElementCollection
    @CollectionTable(name = "regiao_cidades", joinColumns = @JoinColumn(name = "regiao_id"))
    @Builder.Default
    private List<Cidade> cidades = new ArrayList<>();

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

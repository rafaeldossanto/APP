package com.app.APP.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cidade que compoe uma regiao. Sub-item sem id proprio (@Embeddable);
 * persistido como @ElementCollection na Regiao (tabela regiao_cidades).
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cidade {

    @Column(nullable = false)
    private String nome;

    private Double latitude;
    private Double longitude;
    private Double altitude;
}

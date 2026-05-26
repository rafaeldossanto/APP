package com.app.APP.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "regioes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regiao {

    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    private Double latMin;
    private Double latMax;
    private Double lngMin;
    private Double lngMax;
}
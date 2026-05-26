package com.app.APP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;


@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor
@Immutable
public class Usuario {

    @Id
    private String id;
    private String nome;
    private String email;
    private String codigoUsuario;
}

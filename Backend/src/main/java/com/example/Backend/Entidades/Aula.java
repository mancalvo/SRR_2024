package com.example.Backend.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aula") // Tabla base
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de herencia para crear tablas separadas
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Aula {
    @Id
    @Column(name = "numero")
    private Integer numero;

    @Column(name = "aire_acondicionado")
    private Boolean aireAcondicionado;

    @Column(name = "pizarron")
    private Boolean pizarron;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "piso")
    private Integer piso;
}


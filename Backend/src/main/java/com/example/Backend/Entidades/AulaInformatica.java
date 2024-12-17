package com.example.Backend.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aula_informatica") // Tabla específica para esta subclase
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaInformatica extends Aula {
    @Column(name = "cant_pc")
    private Integer cantPC;

    @Column(name = "canion")
    private Boolean canion;
}


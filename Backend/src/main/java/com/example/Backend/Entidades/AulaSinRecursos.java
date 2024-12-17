package com.example.Backend.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aula_sin_recursos") // Tabla específica para esta subclase
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaSinRecursos extends Aula {
    @Column(name = "ventilador")
    private Boolean ventilador;
}



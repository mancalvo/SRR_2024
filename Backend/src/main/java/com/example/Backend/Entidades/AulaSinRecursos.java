package com.example.Backend.Entidades;

import com.example.Backend.DTO.AulaDTO;
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

    @Override
    public AulaDTO toDto() {
        Integer num = this.getNumero();
        int cap = this.getCapacidad();
        String tAula = "SINRECURSOS";

        return new AulaDTO(num, cap, tAula);   
    }
}



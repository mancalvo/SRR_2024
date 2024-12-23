package com.example.Backend.Entidades;

import com.example.Backend.DTO.AulaDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aula_multimedios") // Tabla específica para esta subclase
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaMultimedios extends Aula {
    @Column(name = "ventilador")
    private Boolean ventilador;

    @Column(name = "tv")
    private Boolean tv;

    @Column(name = "canion")
    private Boolean canion;

    @Column(name = "computadora")
    private Boolean computadora;

    @Override
    public AulaDTO toDto() {
        Integer num = this.getNumero();
        int cap = this.getCapacidad();
        String tAula = "MULTIMEDIOS";

        return new AulaDTO(num, cap, tAula);   
    }
}




package com.example.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AulaDTO {

    private String nombre; // Nombre del aula
    private int capacidad;
    private String tipoAula;

}

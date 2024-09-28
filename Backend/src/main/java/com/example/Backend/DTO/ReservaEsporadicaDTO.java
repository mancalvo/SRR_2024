package com.example.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservaEsporadicaDTO {
    private String solicitante;
    private String correo;
    private String catedra;
    private LocalDate fecha;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;
    private Long aulaId; // ID del aula asociada
    private int cantidadAlumnos;
}

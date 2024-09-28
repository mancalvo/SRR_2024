package com.example.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservaPeriodicaDTO {
    private String solicitante;
    private String correo;
    private String catedra;
    private String periodo; // "1 Cuatrimestre", "2 Cuatrimestres", "Anual"
    private int cantidadAlumnos;
    private List<DiaReservaDTO> diasReserva;
}

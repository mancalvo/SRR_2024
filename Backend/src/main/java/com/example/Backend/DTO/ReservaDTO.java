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
public class ReservaDTO {
    private String solicitante;
    private String correo;
    private String catedra;
    private String tipoReserva; // Periodica o Esporádica
    private String periodo; //1 Cuatrimestre,2 Cuatrimestres,Anual. Este campo podría ser null en las reservas esporádicas
    private int cantidadAlumnos;
    private List<DiaReservaDTO> diasReserva;
}

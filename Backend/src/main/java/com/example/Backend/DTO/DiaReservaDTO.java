package com.example.Backend.DTO;

import com.example.Backend.Enum.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class DiaReservaDTO {
    private DiaSemana diaSemana;
    private LocalDate fecha;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;
    private Long aulaId;
}

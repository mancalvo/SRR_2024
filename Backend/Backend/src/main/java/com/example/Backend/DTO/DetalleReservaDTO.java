package com.example.Backend.DTO;

import com.example.Backend.Enum.DiaSemana;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleReservaDTO {
    private DiaSemana diaSemana; // Solo para reservas periódicas

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha; // Solo para reservas esporádicas

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioFinal;
    private Integer aulaId; // Identificador del aula
}

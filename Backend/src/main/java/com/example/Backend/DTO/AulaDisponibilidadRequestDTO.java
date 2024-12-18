package com.example.Backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AulaDisponibilidadRequestDTO {

    private String tipoAula; // Tipo de aula: "multimedio", "informatica", etc.
    private int capacidad;   // Capacidad mínima requerida
    private String tipoReserva; // "ESPORADICA" o "PERIODICA"
    private String tipoPeriodo; // "PRIMER_CUATRIMESTRE", "SEGUNDO_CUATRIMESTRE" o "ANUAL" (si es periódica)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha; // Fecha de la reserva (solo esporádica)
    private String dia; // Día de la semana (solo periódica)
    @JsonFormat(pattern = "HH:mm")
    private String horaInicio; // Hora de inicio (formato HH:mm)
    @JsonFormat(pattern = "HH:mm")
    private String horaFinal; // Hora de fin (formato HH:mm)

}

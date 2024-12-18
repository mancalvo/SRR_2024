package com.example.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AulaDisponibilidadResponseDTO {
    private List<AulaDTO> aulasDisponibles; // Lista de aulas que cumplen con los criterios
    private List<ReservaSolapadaDTO> conflictos; // Detalle de conflictos si no hay aulas disponibles
}

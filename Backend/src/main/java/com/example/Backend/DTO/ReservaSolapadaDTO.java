package com.example.Backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaSolapadaDTO {
    private List<ReservaEsporadicaSolapadaDTO> conflictosEsporadicos;
    private List<ReservaPeriodicaSolapadaDTO> conflictosPeriodicos;
}

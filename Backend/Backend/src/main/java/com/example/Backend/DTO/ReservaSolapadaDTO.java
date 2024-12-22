package com.example.Backend.DTO;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaSolapadaDTO {
    private List<ReservaEsporadicaSolapadaDTO> conflictosEsporadicos;
    private List<ReservaPeriodicaSolapadaDTO> conflictosPeriodicos;
}

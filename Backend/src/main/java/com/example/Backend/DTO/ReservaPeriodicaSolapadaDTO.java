package com.example.Backend.DTO;

import com.example.Backend.Entidades.DiaPeriodica;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaPeriodicaSolapadaDTO {

    private String docente; // Apellido y nombre del docente
    private String curso;   // Nombre de la cátedra/seminario
    private String email;   // Correo electrónico de contacto
    private String horaInicio;
    private String horaFinal;

    public static List<ReservaPeriodicaSolapadaDTO> crearListaReservasPeriodicasSolapadaDTO(
            List<DiaPeriodica> periodicas) {
        if (periodicas == null) {
            return null;
        }
        return periodicas.stream()
                .map(per -> new ReservaPeriodicaSolapadaDTO(
                per.getReserva().getSolicitante(),
                per.getReserva().getCatedra(),
                per.getReserva().getCorreo(),
                per.getHoraInicio().toString(),
                per.getHoraFinal().toString()))
                .collect(Collectors.toList());
    }
}

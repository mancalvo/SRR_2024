package com.example.Backend.DTO;

import com.example.Backend.Entidades.DiaEsporadica;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEsporadicaSolapadaDTO {

    private String docente; // Apellido y nombre del docente
    private String curso;   // Nombre de la cátedra/seminario
    private String email;   // Correo electrónico de contacto
    private String horaInicio;
    private String horaFinal;

    public static List<ReservaEsporadicaSolapadaDTO> crearListaReservasEsporadicasSolapadaDTO(
            List<DiaEsporadica> esporadicas) {
        if (esporadicas == null) {
            return null;
        }
        return esporadicas.stream()
                .map(esp -> new ReservaEsporadicaSolapadaDTO(
                esp.getReserva().getSolicitante(),
                esp.getReserva().getCatedra(),
                esp.getReserva().getCorreo(),
                esp.getHoraInicio().toString(),
                esp.getHoraFinal().toString()))
                .collect(Collectors.toList());
    }

}

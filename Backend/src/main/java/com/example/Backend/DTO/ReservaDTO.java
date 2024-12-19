package com.example.Backend.DTO;

import com.example.Backend.Enum.Tipo_Aula;
import com.example.Backend.Enum.Tipo_Periodo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private String nombreUsuario;
    private String solicitante;
    private String correo;
    private String catedra;
    private LocalDate fechaRealizada;
    private String tipoReserva; // "Periódica" o "Esporádica"
    private Tipo_Periodo periodo;
    private Tipo_Aula tipoAula;
    private Integer cantidadAlumnos;

    private List<DetalleReservaDTO> detalleReserva;

}

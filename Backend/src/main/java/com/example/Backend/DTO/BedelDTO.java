package com.example.Backend.DTO;

import com.example.Backend.Enum.Tipo_Turno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BedelDTO {

    private Integer idUsuario;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private Tipo_Turno tipoTurno;

}

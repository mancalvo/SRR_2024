package com.example.Backend.DTO;

import com.example.Backend.Enum.Tipo_Turno;
import com.example.Backend.Enum.Tipo_Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String contrasenia;
    private String repetirContrasenia;
    private Tipo_Usuario tipoUsuario;
    private Tipo_Turno tipoTurno;
    private Boolean activo;
}


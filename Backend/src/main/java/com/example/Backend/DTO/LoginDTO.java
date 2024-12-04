package com.example.Backend.DTO;

import com.example.Backend.Enum.Tipo_Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String nombreUsuario;
    private String contrasenia;
    private Tipo_Usuario tipoUsuario;
    private Boolean habilitado;
    private String mensaje;

}

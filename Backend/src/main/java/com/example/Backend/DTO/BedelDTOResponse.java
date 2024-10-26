package com.example.Backend.DTO;

import com.example.Backend.Enum.Tipo_Turno;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BedelDTOResponse {
    private Long id;
    private String apellido;
    private String nombre;
    private Tipo_Turno turno;
}

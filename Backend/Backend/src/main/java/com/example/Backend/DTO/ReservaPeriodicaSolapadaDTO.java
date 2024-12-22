package com.example.Backend.DTO;


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
}

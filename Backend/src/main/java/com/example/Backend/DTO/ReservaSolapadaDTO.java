package com.example.Backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaSolapadaDTO {
    private String docente;
    private String curso;
    private String email;
    private String horaInicio;
    private String horaFinal;
}

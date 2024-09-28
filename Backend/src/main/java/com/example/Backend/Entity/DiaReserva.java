package com.example.Backend.Entity;

import com.example.Backend.Enum.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaReserva {

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;

    @ManyToOne
    private Aula aula;

}

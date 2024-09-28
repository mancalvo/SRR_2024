package com.example.Backend.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Esporadica")
public class ReservaEsporadica extends Reserva {

    private LocalDate fecha;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;

    @ManyToOne
    private Aula aula;
}

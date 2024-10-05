package com.example.Backend.Entity;

import com.example.Backend.Enum.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservaPeriodicaDiasReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;

    @ManyToOne
    @JoinColumn(name = "aula_id")
    private Aula aula;

    @ManyToOne
    @JoinColumn(name = "reserva_periodica_id", nullable = false)
    private ReservaPeriodica reservaPeriodica;


}

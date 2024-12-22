package com.example.Backend.Entidades;

import com.example.Backend.Enum.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Entity
@Table(name = "dia_periodica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaPeriodica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diaPeriodica")
    private Integer idDiaPeriodica;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_final")
    private LocalTime horaFinal;

    @Enumerated(EnumType.STRING) // Explicitly map the enum
    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @ManyToOne
    @JoinColumn(name = "numero_aula")
    private Aula aula;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private ReservaPeriodica reserva;
}

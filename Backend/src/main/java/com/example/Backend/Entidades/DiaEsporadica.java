package com.example.Backend.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "dia_esporadica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaEsporadica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diaEsporadica")
    private Integer idDiaEsporadica;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_final")
    private LocalTime horaFinal;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "numero_aula")
    private Aula aula;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private ReservaEsporadica reserva;
}
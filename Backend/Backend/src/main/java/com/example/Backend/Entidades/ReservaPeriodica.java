package com.example.Backend.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva_periodica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaPeriodica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservaPeriodica")
    private Integer idReservaPeriodica;

    @Column(name = "cantidad_alumnos")
    private Integer cantidadAlumnos;

    @Column(name = "solicitante")
    private String solicitante;

    @Column(name = "catedra")
    private String catedra;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "correo")
    private String correo;

    @Column(name = "periodosId")
    private ArrayList<Integer> periodosId;

    @ManyToOne
    @JoinColumn(name = "id_bedel")
    private Usuario bedel;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiaPeriodica> diasPeriodica  = new ArrayList<>();;
}
package com.example.Backend.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva_esporadica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEsporadica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservaEsporadica")
    private Integer idReservaEsporadica;

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

    @ManyToOne
    @JoinColumn(name = "id_bedel")
    private Usuario bedel;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiaEsporadica> diasEsporadica = new ArrayList<>();
}
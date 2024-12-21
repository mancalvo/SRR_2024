package com.example.Backend.Entidades;

import com.example.Backend.Enum.Tipo_Aula;
import com.example.Backend.Enum.Tipo_Periodo;
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

    @Column(name = "solicitante")
    private String solicitante;

    @Column(name = "correo")
    private String correo;

    @Column(name = "catedra")
    private String catedra;

    @Column(name = "cantidad_alumnos")
    private Integer cantidadAlumnos;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "tipo_periodo")
    @Enumerated(EnumType.STRING)
    private Tipo_Periodo tipoPeriodo;

    @Transient  // No se mapea con la BBDD
    private Tipo_Aula tipoAula;

    @Transient // No se mapea con la BBDD
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "id_bedel")
    private Usuario bedel;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiaPeriodica> diasPeriodica  = new ArrayList<>();;
}
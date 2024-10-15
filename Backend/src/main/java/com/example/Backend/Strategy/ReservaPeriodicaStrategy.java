package com.example.Backend.Strategy;

import com.example.Backend.Entity.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Exceptions.AulaNoDisponibleException;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
import com.example.Backend.Repository.ReservaPeriodicaDiasReservaRepository;
import com.example.Backend.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaPeriodicaStrategy implements ReservaStrategy {

    private final ReservaPeriodicaDiasReservaRepository reservaPeriodicaDiasReservaRepository;
    private final AulaRepository aulaRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaPeriodicaStrategy(ReservaPeriodicaDiasReservaRepository reservaPeriodicaDiasReservaRepository,
                                    AulaRepository aulaRepository, ReservaRepository reservaRepository) {
        this.reservaPeriodicaDiasReservaRepository = reservaPeriodicaDiasReservaRepository;
        this.aulaRepository = aulaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    @Transactional
    public void procesarReserva(Reserva reserva) {
        if (!(reserva instanceof ReservaPeriodica)) {
            throw new IllegalArgumentException("La reserva proporcionada no es de tipo periódica");
        }

        ReservaPeriodica reservaPeriodica = (ReservaPeriodica) reserva;

        validarDatosEntrada(reservaPeriodica);

        // Establecer la relación en cada ReservaPeriodicaDiasReserva
        for (ReservaPeriodicaDiasReserva diaReserva : reservaPeriodica.getDiasReserva()) {
            diaReserva.setReservaPeriodica(reservaPeriodica); // Asegúrate de que la relación esté establecida

            // Verificar que el aula no sea nula
            if (diaReserva.getAula() == null) {
                throw new ReservaDataException("El aula no puede ser nula para el día: " + diaReserva.getDiaSemana());
            }

            // Obtener reservas existentes para esa aula y día, excluyendo la reserva actual
            List<ReservaPeriodicaDiasReserva> reservasExistentes = reservaPeriodicaDiasReservaRepository.findByAulaAndDiaSemana(
                            diaReserva.getAula(), diaReserva.getDiaSemana()).stream()
                    .filter(r -> !r.getReservaPeriodica().getId().equals(reservaPeriodica.getId()))
                    .collect(Collectors.toList());

            // Validar si alguna reserva existente se solapa en horario
            for (ReservaPeriodicaDiasReserva reservaPeriodicaDiasReserva : reservasExistentes) {
                if (horariosSeSolapan(diaReserva.getHorarioInicio(), diaReserva.getHorarioFinal(),
                        reservaPeriodicaDiasReserva.getHorarioInicio(), reservaPeriodicaDiasReserva.getHorarioFinal())) {
                    throw new AulaNoDisponibleException("El aula " + diaReserva.getAula().getNumero() +
                            " no está disponible el día " + diaReserva.getDiaSemana() +
                            " de " + diaReserva.getHorarioInicio() + " a " + diaReserva.getHorarioFinal());
                }
            }
        }
        reservaRepository.save(reservaPeriodica);
    }
    private void validarDatosEntrada(ReservaPeriodica reservaPeriodica) {
        if (reservaPeriodica.getPeriodo() == null || reservaPeriodica.getPeriodo().isEmpty()) {
            throw new ReservaDataException("El periodo de la reserva periódica no puede estar vacío.");
        }
        if (reservaPeriodica.getCantidadAlumnos() <= 0) {
            throw new ReservaDataException("La cantidad de alumnos debe ser mayor a cero.");
        }
    }

    private boolean horariosSeSolapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    @Override
    public boolean soporta(String tipoReserva) {
        return "PERIODICA".equalsIgnoreCase(tipoReserva);
    }

    private int convertirDiaSemanaADiaSemanaOrdinal(DiaSemana diaSemana) {
        switch (diaSemana) {
            case LUNES:
                return 2;
            case MARTES:
                return 3;
            case MIERCOLES:
                return 4;
            case JUEVES:
                return 5;
            case VIERNES:
                return 6;
            case SABADO:
                return 7;
            default:
                throw new ReservaDataException("No se puede realizar reserva el dia " + diaSemana);
        }
    }
}

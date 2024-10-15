package com.example.Backend.Strategy;

import com.example.Backend.Entity.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Exceptions.AulaNoDisponibleException;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
import com.example.Backend.Repository.ReservaEsporadicaRepository;
import com.example.Backend.Repository.ReservaPeriodicaDiasReservaRepository;
import com.example.Backend.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaEsporadicaStrategy implements ReservaStrategy {

    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final ReservaPeriodicaDiasReservaRepository reservaPeriodicaDiasReservaRepository;
    private final AulaRepository aulaRepository;

    @Autowired
    public ReservaEsporadicaStrategy(ReservaEsporadicaRepository reservaEsporadicaRepository,
                                     ReservaPeriodicaDiasReservaRepository reservaPeriodicaDiasReservaRepository,
                                     AulaRepository aulaRepository) {
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.reservaPeriodicaDiasReservaRepository = reservaPeriodicaDiasReservaRepository;
        this.aulaRepository = aulaRepository;
    }

    @Override
    @Transactional
    public void procesarReserva(Reserva reserva) {
        if (!(reserva instanceof ReservaEsporadica)) {
            throw new IllegalArgumentException("La reserva proporcionada no es de tipo esporádica");
        }

        ReservaEsporadica reservaEsporadica = (ReservaEsporadica) reserva;

        validarDatosEntrada(reservaEsporadica);

        Aula aula = reservaEsporadica.getAula();
        if (aula == null) {
            throw new ReservaDataException("El aula no puede ser nula.");
        }

        // Obtener reservas esporádicas existentes para esa aula y fecha, excluyendo la reserva actual
        List<ReservaEsporadica> reservasExistentes = reservaEsporadicaRepository.findByAulaAndFecha(
                        aula, reservaEsporadica.getFecha()).stream()
                .filter(r -> !r.getId().equals(reservaEsporadica.getId()))
                .collect(Collectors.toList());

        // Validar si alguna reserva existente se solapa en horario
        for (ReservaEsporadica reservaExistente : reservasExistentes) {
            if (horariosSeSolapan(reservaEsporadica.getHorarioInicio(), reservaEsporadica.getHorarioFinal(),
                    reservaExistente.getHorarioInicio(), reservaExistente.getHorarioFinal())) {
                throw new AulaNoDisponibleException("El aula " + aula.getNumero() +
                        " no está disponible en la fecha " + reservaEsporadica.getFecha() +
                        " de " + reservaEsporadica.getHorarioInicio() + " a " + reservaEsporadica.getHorarioFinal());
            }
        }

        // Obtener reservas periódicas existentes para esa aula y día de la semana
        DiaSemana diaSemana = convertirDayOfWeekADiaSemana(reservaEsporadica.getFecha().getDayOfWeek());
        List<ReservaPeriodicaDiasReserva> reservasPeriodicas = reservaPeriodicaDiasReservaRepository.findByAulaAndDiaSemana(
                        aula, diaSemana).stream()
                .filter(r -> !r.getReservaPeriodica().getId().equals(reservaEsporadica.getId()))
                .collect(Collectors.toList());

        for (ReservaPeriodicaDiasReserva reservaPeriodicaDiasReserva : reservasPeriodicas) {
            if (horariosSeSolapan(reservaEsporadica.getHorarioInicio(), reservaEsporadica.getHorarioFinal(),
                    reservaPeriodicaDiasReserva.getHorarioInicio(), reservaPeriodicaDiasReserva.getHorarioFinal())) {
                throw new AulaNoDisponibleException("El aula " + aula.getNumero() +
                        " no está disponible el día " + reservaPeriodicaDiasReserva.getDiaSemana() +
                        " de " + reservaEsporadica.getHorarioInicio() + " a " + reservaEsporadica.getHorarioFinal());
            }
        }

        reservaEsporadicaRepository.save(reservaEsporadica);
    }

    @Override
    public boolean soporta(String tipoReserva) {
        return "ESPORADICA".equalsIgnoreCase(tipoReserva);
    }

    private void validarDatosEntrada(ReservaEsporadica reservaEsporadica) {
        if (reservaEsporadica.getFecha() == null || reservaEsporadica.getFecha().isBefore(LocalDate.now())) {
            throw new ReservaDataException("La fecha de la reserva esporádica es inválida.");
        }

        if (reservaEsporadica.getAula() == null) {
            throw new ReservaDataException("El aula no puede ser nula.");
        }

        if (reservaEsporadica.getCantidadAlumnos() <= 0) {
            throw new ReservaDataException("La cantidad de alumnos debe ser mayor a cero.");
        }

        if (reservaEsporadica.getHorarioInicio() == null || reservaEsporadica.getHorarioFinal() == null) {
            throw new ReservaDataException("Los horarios de inicio y finalización no pueden ser nulos.");
        }

        if (reservaEsporadica.getHorarioInicio().isAfter(reservaEsporadica.getHorarioFinal())) {
            throw new ReservaDataException("El horario de inicio debe ser antes del horario final.");
        }
    }

    private boolean horariosSeSolapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    private DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return DiaSemana.LUNES;
            case TUESDAY:
                return DiaSemana.MARTES;
            case WEDNESDAY:
                return DiaSemana.MIERCOLES;
            case THURSDAY:
                return DiaSemana.JUEVES;
            case FRIDAY:
                return DiaSemana.VIERNES;
            case SATURDAY:
                return DiaSemana.SABADO;
            default:
                throw new ReservaDataException("No se puede realizar reserva el día Domingo");
        }
    }
}



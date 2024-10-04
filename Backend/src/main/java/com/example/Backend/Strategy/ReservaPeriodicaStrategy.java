package com.example.Backend.Strategy;

import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.DiaReserva;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Exceptions.AulaNoDisponibleException;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
import com.example.Backend.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class ReservaPeriodicaStrategy implements ReservaStrategy {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private AulaRepository aulaRepository;

    @Override
    public void procesarReserva(Reserva reserva) {
        ReservaPeriodica reservaPeriodica = (ReservaPeriodica) reserva;

        validarDatosEntrada(reservaPeriodica);

        // Validar los días de la semana
        for (DiaReserva diaReserva : reservaPeriodica.getDiasReserva()) {
            if (diaReserva.getAula() == null) {
                throw new ReservaDataException("El aula no puede ser nula para el día: " + diaReserva.getDiaSemana());
            }

            // Validar disponibilidad del aula
            if (!aulaDisponible(diaReserva, reservaPeriodica.getCantidadAlumnos())) {
                throw new AulaNoDisponibleException("El aula " + diaReserva.getAula().getNumero() + " no está disponible el día " + diaReserva.getDiaSemana() + " en el horario " + diaReserva.getHorarioInicio() + " a " + diaReserva.getHorarioFinal());
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


    private boolean aulaDisponible(DiaReserva diaReserva, int cantidadAlumnos) {
        Aula aula = aulaRepository.getById(diaReserva.getAula().getId());
        DiaSemana diaSemana = diaReserva.getDiaSemana();
        LocalTime horarioInicio = diaReserva.getHorarioInicio();
        LocalTime horarioFinal = diaReserva.getHorarioFinal();

        // Convertir DiaSemana a ordinal para la consulta de ReservaEsporadica
        int diaSemanaOrdinal = convertirDiaSemanaADiaSemanaOrdinal(diaSemana);

        // Verificar solapamiento con ReservaPeriodica
        boolean existeReservaPeriodica = reservaRepository.existsReservaPeriodicaOverlap(
                aula, diaSemana, horarioFinal, horarioInicio);

        // Verificar solapamiento con ReservaEsporadica
        boolean existeReservaEsporadica = reservaRepository.existsReservaEsporadicaOverlap(
                aula, diaSemanaOrdinal, horarioFinal, horarioInicio);

        if (existeReservaEsporadica || existeReservaPeriodica) {
            return false;
        }

        // Verificar capacidad del aula
        if (cantidadAlumnos > aula.getCapacidad()) {
            throw new AulaNoDisponibleException("La capacidad del aula " + aula.getNumero() + " es insuficiente para la cantidad de alumnos: " + cantidadAlumnos);
        }

        return true;
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
                throw new ReservaDataException("DiaSemana desconocido: " + diaSemana);
        }
    }
}

package com.example.Backend.Strategy;

import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaEsporadica;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Exceptions.AulaNoDisponibleException;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
import com.example.Backend.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservaEsporadicaStrategy  implements ReservaStrategy {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private AulaRepository aulaRepository;

    @Override
    public void procesarReserva(Reserva reserva) {
        ReservaEsporadica reservaEsporadica = (ReservaEsporadica) reserva;

       validarDatosEntrada(reservaEsporadica);

        // Validar la disponibilidad del aula (puedes implementar una lógica de disponibilidad aquí)
        if (!aulaDisponible(reservaEsporadica)) {
            throw new AulaNoDisponibleException("El aula " + reservaEsporadica.getAula().getNumero()  + " no está disponible en la fecha " + reservaEsporadica.getFecha() + " de " + reservaEsporadica.getHorarioInicio() + " a " + reservaEsporadica.getHorarioFinal());
        }

       reservaRepository.save(reservaEsporadica);
    }


    private boolean aulaDisponible(ReservaEsporadica reserva) {
        Aula aula = aulaRepository.getById(reserva.getAula().getId());

        LocalTime horarioInicio = reserva.getHorarioInicio();
        LocalTime horarioFinal = reserva.getHorarioFinal();
        LocalDate fechaReserva = reserva.getFecha();

        boolean existeReservaEsporadica = reservaRepository.existsReservaEsporadicaOverlap(
                aula.getId(), fechaReserva, horarioInicio, horarioFinal);


        DiaSemana diaSemana = convertirDiaSemanaADiaSemanaOrdinal(fechaReserva.getDayOfWeek());
        boolean existeReservaPeriodica = reservaRepository.existsReservaPeriodicaOverlap(
                aula.getId(), diaSemana, horarioInicio, horarioFinal);

        if (existeReservaEsporadica || existeReservaPeriodica) {
            return false;
        }

        if (reserva.getCantidadAlumnos() > aula.getCapacidad()) {
            throw new AulaNoDisponibleException("La capacidad del aula " + aula.getNumero() + " es insuficiente para la cantidad de alumnos: " + reserva.getCantidadAlumnos());
        }

        return true;
    }

    private void validarDatosEntrada(ReservaEsporadica reservaEsporadica) {
        if (reservaEsporadica.getFecha() == null || reservaEsporadica.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la reserva esporádica es inválida.");
        }

        if (reservaEsporadica.getAula() == null) {
            throw new IllegalArgumentException("El aula no puede ser nula.");
        }

    }

    private DiaSemana convertirDiaSemanaADiaSemanaOrdinal(DayOfWeek dayOfWeek) {
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
                throw new ReservaDataException("DiaSemana desconocido: " + dayOfWeek);
        }
    }
}

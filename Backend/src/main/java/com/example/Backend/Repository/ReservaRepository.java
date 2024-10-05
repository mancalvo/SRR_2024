package com.example.Backend.Repository;

import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Enum.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT COUNT(r) > 0 FROM ReservaPeriodica r JOIN r.diasReserva d WHERE d.aula = :aula AND d.diaSemana = :diaSemana AND " +
            "((d.horarioInicio < :horarioFinal) AND (d.horarioFinal > :horarioInicio))")
    boolean existsReservaPeriodicaOverlap(Aula aula, DiaSemana diaSemana, LocalTime horarioInicio, LocalTime horarioFinal);

    @Query("SELECT COUNT(r) > 0 FROM ReservaEsporadica r WHERE r.aula = :aula AND FUNCTION('DAYOFWEEK', r.fecha) = :diaSemanaOrdinal AND " +
            "((r.horarioInicio < :horarioFinal) AND (r.horarioFinal > :horarioInicio))")
    boolean existsReservaEsporadicaOverlap(Aula aula, int diaSemanaOrdinal, LocalTime horarioInicio, LocalTime horarioFinal);

    // Para verificar solapamiento con reservas esporádicas en una fecha y rango horario específico
    @Query("SELECT COUNT(r) > 0 FROM ReservaEsporadica r WHERE r.aula.id = :aulaId AND r.fecha = :fecha AND " +
            "((r.horarioInicio < :fin) AND (r.horarioFinal > :inicio))")
    boolean existsReservaEsporadicaOverlap(Long aulaId, LocalDate fecha, LocalTime inicio, LocalTime fin);

    // Para verificar solapamiento con reservas periódicas en un día de la semana y rango horario específico
    @Query("SELECT COUNT(r) > 0 FROM ReservaPeriodica r JOIN r.diasReserva d WHERE d.aula.id = :aulaId AND d.diaSemana = :diaSemana AND " +
            "((d.horarioInicio < :fin) AND (d.horarioFinal > :inicio))")
    boolean existsReservaPeriodicaOverlap(Long aulaId, DiaSemana diaSemana, LocalTime inicio, LocalTime fin);
}


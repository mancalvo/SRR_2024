package com.example.Backend.DAO;

import com.example.Backend.Entidades.DiaPeriodica;
import com.example.Backend.Entidades.ReservaPeriodica;
import com.example.Backend.Enum.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaPeriodicaDAO extends JpaRepository<ReservaPeriodica, Integer> {

    @Query("SELECT rp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.aula.numero = :aulaId")
    List<ReservaPeriodica> findByDiasPeriodica_Aula_Numero(@Param("aulaId") Integer aulaId);

    @Query("SELECT dp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.diaSemana = :diaSemana " +
            "AND rp.fecha >= :fechaInicio " +
            "AND rp.fecha <= :fechaFinal")
    List<DiaPeriodica> findByDiaSemanaAndFechas(@Param("diaSemana") DiaSemana diaSemana,
                                                @Param("fechaInicio") LocalDate fechaInicio,
                                                @Param("fechaFinal") LocalDate fechaFinal);

}


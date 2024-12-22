package com.example.Backend.DAO;

import com.example.Backend.Entidades.DiaPeriodica;
import com.example.Backend.Entidades.ReservaPeriodica;
import com.example.Backend.Enum.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaPeriodicaDAO extends JpaRepository<ReservaPeriodica, Integer> {
    // List<ReservaPeriodica> findByDiasPeriodica_Aula_Numero(Integer aulaId);

    @Query("SELECT rp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.aula.numero = :aulaId")
    List<ReservaPeriodica> findByDiasPeriodica_Aula_Numero(@Param("aulaId") Integer aulaId);
    
    @Query("SELECT dp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.diaSemana = :diaSemana " +
            "AND EXISTS (SELECT 1 FROM rp.periodosId pid WHERE pid IN :periodos) " +
            "AND dp.aula.numero IN :aulaIds")
    List<DiaPeriodica> findByDiaSemanaAndPeriodosAndAulaIds(@Param("diaSemana") DiaSemana diaSemana,
                                                            @Param("periodos") List<Integer> periodos,
                                                            @Param("aulaIds") List<Integer> aulaIds);
    
    @Query("SELECT dp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.diaSemana = :diaSemana " +
            "AND EXISTS (SELECT 1 FROM rp.periodosId pid WHERE pid = :periodo) " +
            "AND dp.aula.numero IN :aulaIds")
    List<DiaPeriodica> findByDiaSemanaAndPeriodoAndAulaIds(@Param("diaSemana") DiaSemana diaSemana,
                                                            @Param("periodo") Integer periodo,
                                                            @Param("aulaIds") List<Integer> aulaIds);
    
        @Query("SELECT dp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.diaSemana = :diaSemana " +
            "AND EXISTS (SELECT 1 FROM rp.periodosId pid WHERE pid IN :periodos) " +
            "AND dp.aula.numero = :aulaId")
    List<DiaPeriodica> findByDiaSemanaAndPeriodosAndAulaId(@Param("diaSemana") DiaSemana diaSemana,
                                                            @Param("periodos") List<Integer> periodos,
                                                            @Param("aulaId") Integer aulaId);
    
            @Query("SELECT dp FROM ReservaPeriodica rp " +
            "JOIN rp.diasPeriodica dp " +
            "WHERE dp.diaSemana = :diaSemana " +
            "AND EXISTS (SELECT 1 FROM rp.periodosId pid WHERE pid = :periodo) " +
            "AND dp.aula.numero = :aulaId")
    List<DiaPeriodica> findByDiaSemanaAndPeriodoAndAulaId(@Param("diaSemana") DiaSemana diaSemana,
                                                            @Param("periodo") Integer periodo,
                                                            @Param("aulaId") Integer aulaId);
    
}


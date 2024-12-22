package com.example.Backend.DAO;

import com.example.Backend.Entidades.DiaEsporadica;
import com.example.Backend.Entidades.ReservaEsporadica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaEsporadicaDAO extends JpaRepository<ReservaEsporadica, Integer> {
    boolean existsByDiasEsporadica_FechaAndDiasEsporadica_Aula_Numero(LocalDate fecha, Integer aulaId);
    
    @Query("SELECT re FROM ReservaEsporadica re WHERE re.fecha = :fecha")
    List<DiaEsporadica> findByFecha(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT de FROM DiaEsporadica de WHERE de.fecha IN :fecha AND de.aula.numero = :aulaId")
    List<DiaEsporadica> findDiaEsporadicaByFechasAndAulaId(@Param("fecha") List<LocalDate> fecha,
            @Param("aulaId") Integer aulaId);
    
    @Query("SELECT de FROM DiaEsporadica de WHERE de.fecha = :fecha AND de.aula.numero = :aulaId")
    List<DiaEsporadica> findDiaEsporadicaByFechaAndAulaId(@Param("fecha") LocalDate fecha,
            @Param("aulaId") Integer aulaId);
    
    @Query("SELECT de FROM DiaEsporadica de WHERE de.fecha = :fecha AND de.aula.numero IN :aulaIds")
    List<DiaEsporadica> findDiaEsporadicaByFechaAndAulaIds(@Param("fecha") LocalDate fecha,
            @Param("aulaIds") List<Integer> aulaIds);

    @Query("SELECT de FROM DiaEsporadica de WHERE de.fecha IN :fechas AND de.aula.numero IN :aulaIds")
    List<DiaEsporadica> findDiaEsporadicaByFechasAndAulaIds(@Param("fechas") List<LocalDate> fecha,
            @Param("aulaIds") List<Integer> aulaIds);
}


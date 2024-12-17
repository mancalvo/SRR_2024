package com.example.Backend.DAO;

import com.example.Backend.Entidades.ReservaPeriodica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaPeriodicaDAO extends JpaRepository<ReservaPeriodica, Integer> {
    List<ReservaPeriodica> findByDiasPeriodica_Aula_Numero(Integer aulaId);
}


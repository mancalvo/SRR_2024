package com.example.Backend.DAO;

import com.example.Backend.Entidades.ReservaEsporadica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservaEsporadicaDAO extends JpaRepository<ReservaEsporadica, Integer> {
    boolean existsByDiasEsporadica_FechaAndDiasEsporadica_Aula_Numero(LocalDate fecha, Integer aulaId);

}


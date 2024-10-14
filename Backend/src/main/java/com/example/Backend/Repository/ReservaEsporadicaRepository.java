package com.example.Backend.Repository;

import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.ReservaEsporadica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaEsporadicaRepository extends JpaRepository<ReservaEsporadica, Long> {
    List<ReservaEsporadica> findByAulaAndFecha(Aula aula, LocalDate fecha);
}

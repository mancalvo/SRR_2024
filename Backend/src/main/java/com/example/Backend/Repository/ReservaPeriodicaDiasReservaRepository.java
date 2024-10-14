package com.example.Backend.Repository;

import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.ReservaPeriodicaDiasReserva;
import com.example.Backend.Enum.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaPeriodicaDiasReservaRepository extends JpaRepository<ReservaPeriodicaDiasReserva, Long> {
    List<ReservaPeriodicaDiasReserva> findByAulaAndDiaSemana(Aula aula, DiaSemana diaSemana);
}

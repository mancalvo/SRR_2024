package com.example.Backend.DAO;

import com.example.Backend.Entidades.AulaSinRecursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaSinRecursosDAO extends JpaRepository<AulaSinRecursos, Integer> {
    @Query("SELECT asr FROM AulaSinRecursos asr WHERE asr.capacidad >= :capacidad")
    List<AulaSinRecursos> findByCapacidad(@Param("capacidad") int capacidad);
}

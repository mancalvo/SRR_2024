package com.example.Backend.DAO;

import com.example.Backend.Entidades.AulaInformatica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaInformaticaDAO extends JpaRepository<AulaInformatica, Integer> {

    @Query("SELECT ai FROM AulaInformatica ai WHERE ai.capacidad >= :capacidad")
    List<AulaInformatica> findByCapacidad(@Param("capacidad") int capacidad);

}


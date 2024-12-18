package com.example.Backend.DAO;

import com.example.Backend.Entidades.AulaMultimedios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaMultimediosDAO extends JpaRepository<AulaMultimedios, Integer> {
    @Query("SELECT am FROM AulaMultimedios am WHERE am.capacidad >= :capacidad")
    List<AulaMultimedios> findByCapacidad(@Param("capacidad") int capacidad);
}


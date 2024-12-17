package com.example.Backend.DAO;

import com.example.Backend.Entidades.DiaPeriodica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaPeriodicaDAO extends JpaRepository<DiaPeriodica, Integer> {
}


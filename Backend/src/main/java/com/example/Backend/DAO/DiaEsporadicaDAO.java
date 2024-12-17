package com.example.Backend.DAO;

import com.example.Backend.Entidades.DiaEsporadica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaEsporadicaDAO extends JpaRepository<DiaEsporadica, Integer> {
}

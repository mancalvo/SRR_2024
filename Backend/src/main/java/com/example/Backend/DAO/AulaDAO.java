package com.example.Backend.DAO;

import com.example.Backend.Entidades.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaDAO extends JpaRepository<Aula, Integer> {
}


package com.example.Backend.DAO;

import com.example.Backend.Entidades.AulaSinRecursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaSinRecursosDAO extends JpaRepository<AulaSinRecursos, Integer> {

}

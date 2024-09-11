package com.example.Backend.Repository;

import com.example.Backend.Entity.AulaSinRecursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaSinRecursosRepository extends JpaRepository<AulaSinRecursos,Long> {
}

package com.example.Backend.Services;

import com.example.Backend.Entity.AulaSinRecursos;

import java.util.List;
import java.util.Optional;

public interface IAulaSinRecursosServices {
    void create(AulaSinRecursos aulaSinRecursos);
    void update(AulaSinRecursos aulaSinRecursos);
    void deleteById(Long id);
    Optional<AulaSinRecursos> findById(Long id);
    List<AulaSinRecursos> findAll();
}

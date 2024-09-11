package com.example.Backend.Services;

import com.example.Backend.Entity.AulaSinRecursos;
import com.example.Backend.Repository.AulaSinRecursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaSinRecursosServices implements IAulaSinRecursosServices {

    @Autowired
    private AulaSinRecursosRepository repoAulaSinRecursos;

    @Override
    public void create(AulaSinRecursos aulaSinRecursos) {
        repoAulaSinRecursos.save(aulaSinRecursos);
    }

    @Override
    public void update(AulaSinRecursos aulaSinRecursos) {
        repoAulaSinRecursos.save(aulaSinRecursos);
    }

    @Override
    public void deleteById(Long id) {
        repoAulaSinRecursos.deleteById(id);
    }

    @Override
    public Optional<AulaSinRecursos> findById(Long id) {
        return repoAulaSinRecursos.findById(id);
    }

    @Override
    public List<AulaSinRecursos> findAll() {
        return repoAulaSinRecursos.findAll();
    }
}

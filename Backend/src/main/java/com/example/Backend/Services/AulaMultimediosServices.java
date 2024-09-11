package com.example.Backend.Services;

import com.example.Backend.Entity.AulaMultimedios;
import com.example.Backend.Repository.AulaMultimediosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaMultimediosServices implements IAulaMultimediosServices {

    @Autowired
    private AulaMultimediosRepository repoAulaMultimedios;

    @Override
    public void create(AulaMultimedios aulaMultimedios) {
        repoAulaMultimedios.save(aulaMultimedios);
    }

    @Override
    public void update(AulaMultimedios aulaMultimedios) {
        repoAulaMultimedios.save(aulaMultimedios);
    }

    @Override
    public void deleteById(Long id) {
        repoAulaMultimedios.deleteById(id);
    }

    @Override
    public Optional<AulaMultimedios> findById(Long id) {
        return repoAulaMultimedios.findById(id);
    }

    @Override
    public List<AulaMultimedios> findAll() {
        return repoAulaMultimedios.findAll();
    }
}


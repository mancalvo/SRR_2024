package com.example.Backend.Services;

import com.example.Backend.Entity.AulaMultimedios;

import java.util.List;
import java.util.Optional;

public interface IAulaMultimediosServices {
    void create(AulaMultimedios aulaMultimedios);
    void update(AulaMultimedios aulaMultimedios);
    void deleteById(Long id);
    Optional<AulaMultimedios> findById(Long id);
    List<AulaMultimedios> findAll();
}

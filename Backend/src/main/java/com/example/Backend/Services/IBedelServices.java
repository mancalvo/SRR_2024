package com.example.Backend.Services;

import com.example.Backend.Entity.Bedel;

import java.util.List;
import java.util.Optional;

public interface IBedelServices {
    void create(Bedel bedel);
    void update(Bedel bedel);
    void deleteById(Long id);
    Optional<Bedel> findById(Long id);
    List<Bedel> findAll();
}


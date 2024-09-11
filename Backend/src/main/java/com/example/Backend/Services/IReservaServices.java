package com.example.Backend.Services;

import com.example.Backend.Entity.Reserva;

import java.util.List;
import java.util.Optional;

public interface IReservaServices {
    void create(Reserva reserva);
    void update(Reserva reserva);
    void deleteById(Long id);
    Optional<Reserva> findById(Long id);
    List<Reserva> findAll();
}
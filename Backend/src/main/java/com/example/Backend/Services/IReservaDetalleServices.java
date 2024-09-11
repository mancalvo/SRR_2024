package com.example.Backend.Services;

import com.example.Backend.Entity.ReservaDetalle;

import java.util.List;
import java.util.Optional;

public interface IReservaDetalleServices {
    void create(ReservaDetalle reservaDetalle);
    void update(ReservaDetalle reservaDetalle);
    void deleteById(Long id);
    Optional<ReservaDetalle> findById(Long id);
    List<ReservaDetalle> findAll();
}

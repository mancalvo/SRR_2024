package com.example.Backend.Services;

import com.example.Backend.Entity.ReservaDetalle;
import com.example.Backend.Repository.ReservaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaDetalleServices implements IReservaDetalleServices {

    @Autowired
    private ReservaDetalleRepository repoReservaDetalle;

    @Override
    public void create(ReservaDetalle reservaDetalle) {
        repoReservaDetalle.save(reservaDetalle);
    }

    @Override
    public void update(ReservaDetalle reservaDetalle) {
        repoReservaDetalle.save(reservaDetalle);
    }

    @Override
    public void deleteById(Long id) {
        repoReservaDetalle.deleteById(id);
    }

    @Override
    public Optional<ReservaDetalle> findById(Long id) {
        return repoReservaDetalle.findById(id);
    }

    @Override
    public List<ReservaDetalle> findAll() {
        return repoReservaDetalle.findAll();
    }
}


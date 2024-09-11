package com.example.Backend.Services;

import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaDetalle;
import com.example.Backend.Repository.ReservaDetalleRepository;
import com.example.Backend.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaServices implements IReservaServices {

    @Autowired
    private ReservaRepository repoReserva;

    @Autowired
    private ReservaDetalleRepository repoReservaDetalle; // Asegúrate de tener este repositorio

    @Override
    public void create(Reserva reserva) {
        // Asociar detalles con la reserva
        for (ReservaDetalle detalle : reserva.getDetalles()) {
            detalle.setReserva(reserva); // Asociar cada detalle con la reserva
        }
        repoReserva.save(reserva);
    }

    @Override
    public void update(Reserva reserva) {
        // Recuperar la reserva existente para actualizarla
        Optional<Reserva> optionalReserva = repoReserva.findById(reserva.getId());
        if (optionalReserva.isPresent()) {
            Reserva existingReserva = optionalReserva.get();
            existingReserva.setTipoReserva(reserva.getTipoReserva());
            existingReserva.setPeriodo(reserva.getPeriodo());
            existingReserva.setCuatrimestre(reserva.getCuatrimestre());
            existingReserva.setCantidadAlumnos(reserva.getCantidadAlumnos());

            // Actualizar detalles
            List<ReservaDetalle> updatedDetalles = reserva.getDetalles();
            for (ReservaDetalle detalle : updatedDetalles) {
                detalle.setReserva(existingReserva); // Asociar cada detalle con la reserva existente
            }
            existingReserva.setDetalles(updatedDetalles);

            repoReserva.save(existingReserva);
        }
    }

    @Override
    public void deleteById(Long id) {
        repoReserva.deleteById(id);
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return repoReserva.findById(id);
    }

    @Override
    public List<Reserva> findAll() {
        return repoReserva.findAll();
    }
}

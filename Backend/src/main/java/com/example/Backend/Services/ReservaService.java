package com.example.Backend.Services;

import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaEsporadica;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Repository.ReservaRepository;
import com.example.Backend.Strategy.ReservaContext;
import com.example.Backend.Strategy.ReservaEsporadicaStrategy;
import com.example.Backend.Strategy.ReservaPeriodicaStrategy;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaContext reservaContext;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        this.reservaContext = new ReservaContext();
    }

    public void procesarReserva(Reserva reserva) {
        // Seleccionar la estrategia basada en el tipo de reserva
        if (reserva instanceof ReservaPeriodica) {
            reservaContext.setEstrategia(new ReservaPeriodicaStrategy());
        } else if (reserva instanceof ReservaEsporadica) {
            reservaContext.setEstrategia(new ReservaEsporadicaStrategy());
        } else {
            throw new IllegalArgumentException("Tipo de reserva desconocido");
        }

        // Procesar la reserva con la estrategia seleccionada
        reservaContext.procesar(reserva);

        // Guardar la reserva en la base de datos
        reservaRepository.save(reserva);
    }

}

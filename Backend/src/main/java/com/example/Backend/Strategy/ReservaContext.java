package com.example.Backend.Strategy;

import com.example.Backend.Entity.Reserva;
import com.example.Backend.Exceptions.ReservaDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservaContext {

    private final List<ReservaStrategy> estrategias;

    @Autowired
    public ReservaContext(List<ReservaStrategy> estrategias) {
        this.estrategias = estrategias;
    }

    public void procesarReserva(Reserva reserva) {
        String tipoReserva = reserva.getTipoReserva();
        ReservaStrategy estrategia = estrategias.stream()
                .filter(e -> e.soporta(tipoReserva))
                .findFirst()
                .orElseThrow(() -> new ReservaDataException("No hay estrategia para el tipo de reserva: " + tipoReserva));
        estrategia.procesarReserva(reserva);
    }

}

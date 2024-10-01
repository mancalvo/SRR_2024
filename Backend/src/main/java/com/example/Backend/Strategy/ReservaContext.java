package com.example.Backend.Strategy;

import com.example.Backend.Entity.Reserva;

public class ReservaContext {

    private ReservaStrategy estrategia;

    public void setEstrategia(ReservaStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void procesar(Reserva reserva) {
        if (estrategia == null) {
            throw new IllegalStateException("Estrategia no establecida");
        }
        estrategia.procesarReserva(reserva);
    }

}

package com.example.Backend.Strategy;

import com.example.Backend.Entity.Reserva;

public interface ReservaStrategy {
    void procesarReserva(Reserva reserva);
    boolean soporta(String tipoReserva);
}

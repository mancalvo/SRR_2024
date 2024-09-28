package com.example.Backend.Strategy;

import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaPeriodica;

public class ReservaPeriodicaStrategy implements ReservaStrategy {

    @Override
    public void procesarReserva(Reserva reserva) {
        ReservaPeriodica reservaPeriodica = (ReservaPeriodica) reserva;
        // Lógica específica para procesar reservas periódicas
        System.out.println("Procesando reserva periódica: " + reservaPeriodica.getPeriodo());
        // Aquí puedes agregar más lógica, como validaciones específicas, asignación de aulas, etc.
    }
}

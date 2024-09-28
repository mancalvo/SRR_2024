package com.example.Backend.Strategy;

import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaEsporadica;

public class ReservaEsporadicaStrategy  implements ReservaStrategy {

    @Override
    public void procesarReserva(Reserva reserva) {
        ReservaEsporadica reservaEsporadica = (ReservaEsporadica) reserva;
        // Lógica específica para procesar reservas esporádicas
        System.out.println("Procesando reserva esporádica: " + reservaEsporadica.getFecha());
        // Aquí puedes agregar más lógica, como validaciones específicas, asignación de aulas, etc.
    }
}

package com.example.Backend.Gestores.StrategyValidarDisponibilidadAula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisponibilidadStrategyFactory {

    @Autowired
    private DisponibilidadEsporadicaStrategy disponibilidadEsporadicaStrategy;

    @Autowired
    private DisponibilidadPeriodicaStrategy disponibilidadPeriodicaStrategy;

    public IValidarDisponibilidad getStrategy(String tipoReserva) {
        switch (tipoReserva.toUpperCase()) {
            case "ESPORADICA":
                return disponibilidadEsporadicaStrategy;
            case "PERIODICA":
                return disponibilidadPeriodicaStrategy;
            default:
                throw new IllegalArgumentException("Tipo de reserva no válido: " + tipoReserva);
        }
    }

}

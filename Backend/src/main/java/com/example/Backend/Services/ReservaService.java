package com.example.Backend.Services;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaEsporadica;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Mapper.ReservaMapper;
import com.example.Backend.Repository.ReservaRepository;
import com.example.Backend.Strategy.ReservaContext;
import com.example.Backend.Strategy.ReservaEsporadicaStrategy;
import com.example.Backend.Strategy.ReservaPeriodicaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaContext reservaContext;

    @Autowired
    private ReservaMapper reservaMapper;

    @Autowired
    private ReservaPeriodicaStrategy reservaPeriodicaStrategy;

    @Autowired
    private ReservaEsporadicaStrategy reservaEsporadicaStrategy;

    public void procesarReserva(ReservaDTO reservaDTO) {

        Reserva reserva = reservaMapper.convertirToEntidad(reservaDTO);

        // Seleccionar la estrategia basada en el tipo de reserva
        if (reserva instanceof ReservaPeriodica) {
            reservaContext.setEstrategia(reservaPeriodicaStrategy);
        } else if (reserva instanceof ReservaEsporadica) {
            reservaContext.setEstrategia(reservaEsporadicaStrategy);
        } else {
            throw new ReservaDataException("Tipo de reserva desconocido");
        }

        reservaContext.procesar(reserva);
    }
}

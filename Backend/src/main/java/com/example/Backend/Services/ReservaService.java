package com.example.Backend.Services;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Mapper.ReservaMapper;
import com.example.Backend.Strategy.ReservaContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaContext reservaContext;
    private final ReservaMapper reservaMapper;

    @Autowired
    public ReservaService(ReservaContext reservaContext, ReservaMapper reservaMapper) {
        this.reservaContext = reservaContext;
        this.reservaMapper = reservaMapper;
    }

    @Transactional
    public void procesarReserva(ReservaDTO reservaDTO) {
        Reserva reserva = reservaMapper.convertirToEntidad(reservaDTO);
        reservaContext.procesarReserva(reserva);
    }
}

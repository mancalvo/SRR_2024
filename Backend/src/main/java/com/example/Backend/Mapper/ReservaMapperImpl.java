package com.example.Backend.Mapper;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Exceptions.ReservaDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapperImpl implements ReservaMapper {
    @Autowired
    private ReservaEsporadicaMapper reservaEsporadicaMapper;

    @Autowired
    private ReservaPeriodicaMapper reservaPeriodicaMapper;

    @Override
    public Reserva convertirToEntidad(ReservaDTO reservaDTO) {
        reservaDTO.setTipoReserva(reservaDTO.getTipoReserva().toUpperCase());
        if ("ESPORADICA".equalsIgnoreCase(reservaDTO.getTipoReserva())) {
            return reservaEsporadicaMapper.convertirToReservaEsporadica(reservaDTO);
        } else if ("PERIODICA".equalsIgnoreCase(reservaDTO.getTipoReserva())) {
            return reservaPeriodicaMapper.convertirToReservaPeriodica(reservaDTO);
        } else {
            throw new ReservaDataException("Tipo de reserva no reconocido: " + reservaDTO.getTipoReserva());
        }
    }
}

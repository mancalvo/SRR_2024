package com.example.Backend.Mapper;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Reserva;

public interface ReservaMapper {
    Reserva convertirToEntidad(ReservaDTO reservaDTO);
}

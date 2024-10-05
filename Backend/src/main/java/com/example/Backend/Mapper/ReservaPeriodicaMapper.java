package com.example.Backend.Mapper;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.ReservaPeriodica;
import org.springframework.stereotype.Component;

@Component
public interface ReservaPeriodicaMapper {
    ReservaPeriodica convertirToReservaPeriodica(ReservaDTO dto);
}

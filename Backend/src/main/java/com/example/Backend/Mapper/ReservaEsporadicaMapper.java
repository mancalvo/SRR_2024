package com.example.Backend.Mapper;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.ReservaEsporadica;
import org.springframework.stereotype.Component;

@Component
public interface ReservaEsporadicaMapper {
    ReservaEsporadica convertirToReservaEsporadica(ReservaDTO dto);
}

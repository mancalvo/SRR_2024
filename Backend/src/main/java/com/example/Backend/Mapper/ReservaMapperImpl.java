package com.example.Backend.Mapper;

import com.example.Backend.DTO.DiaReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaEsporadica;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Entity.ReservaPeriodicaDiasReserva;
import com.example.Backend.Exceptions.ReservaDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

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



    public ReservaDTO convertirToReservaPeriodicaDTO(ReservaPeriodica reserva) {
        ReservaDTO dto = new ReservaDTO();
        // Lógica para convertir ReservaPeriodica a ReservaDTO...
        dto.setSolicitante(reserva.getSolicitante());
        dto.setCorreo(reserva.getCorreo());
        dto.setCatedra(reserva.getCatedra());
        dto.setCantidadAlumnos(reserva.getCantidadAlumnos());
        dto.setTipoReserva(reserva.getTipoReserva());
        dto.setPeriodo(reserva.getPeriodo());
        dto.setDiasReserva(reserva.getDiasReserva().stream()
                .map(this::convertirADiaReservaDTO)
                .collect(Collectors.toList()));
        return dto;
    }


    public ReservaDTO convertirToReservaEsporadicaDTO(ReservaEsporadica reserva) {
        ReservaDTO dto = new ReservaDTO();
        // Lógica para convertir ReservaEsporadica a ReservaDTO...
        dto.setSolicitante(reserva.getSolicitante());
        dto.setCorreo(reserva.getCorreo());
        dto.setCatedra(reserva.getCatedra());
        dto.setCantidadAlumnos(reserva.getCantidadAlumnos());
        dto.setTipoReserva(reserva.getTipoReserva());
        DiaReservaDTO diaDTO = new DiaReservaDTO();
        diaDTO.setFecha(reserva.getFecha());
        diaDTO.setHorarioInicio(reserva.getHorarioInicio());
        diaDTO.setHorarioFinal(reserva.getHorarioFinal());
        diaDTO.setAulaId(reserva.getAula().getId());
        dto.setDiasReserva(Collections.singletonList(diaDTO));
        return dto;
    }

    // Método para convertir ReservaPeriodicaDiasReserva a DiaReservaDTO
    private DiaReservaDTO convertirADiaReservaDTO(ReservaPeriodicaDiasReserva diaReserva) {
        DiaReservaDTO dto = new DiaReservaDTO();
        dto.setDiaSemana(diaReserva.getDiaSemana().name());
        dto.setHorarioInicio(diaReserva.getHorarioInicio());
        dto.setHorarioFinal(diaReserva.getHorarioFinal());
        dto.setAulaId(diaReserva.getAula().getId());
        return dto;
    }
}

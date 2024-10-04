package com.example.Backend.Mapper;

import com.example.Backend.DTO.DiaReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReservaMapper {

    @Autowired
    private AulaRepository aulaRepository;

    public Reserva convertirToEntidad(ReservaDTO reservaDTO) {
        reservaDTO.setTipoReserva(reservaDTO.getTipoReserva().toUpperCase());
        if ("ESPORADICA".equalsIgnoreCase(reservaDTO.getTipoReserva())) {
            return convertirToReservaEsporadica(reservaDTO);
        } else if ("PERIODICA".equalsIgnoreCase(reservaDTO.getTipoReserva())) {
            return convertirToReservaPeriodica(reservaDTO);
        } else {
            throw new IllegalArgumentException("Tipo de reserva no reconocido: " + reservaDTO.getTipoReserva());
        }
    }

    private ReservaEsporadica convertirToReservaEsporadica(ReservaDTO dto) {
        ReservaEsporadica reserva = new ReservaEsporadica();
        reserva.setSolicitante(dto.getSolicitante());
        reserva.setCorreo(dto.getCorreo());
        reserva.setCatedra(dto.getCatedra());
        reserva.setCantidadAlumnos(dto.getCantidadAlumnos());

        List<DiaReserva> diasReserva = new ArrayList<>();
        for (DiaReservaDTO diaDTO : dto.getDiasReserva()) {
            DiaReserva diaReserva = new DiaReserva();
            reserva.setHorarioInicio(diaDTO.getHorarioInicio());
            reserva.setHorarioFinal(diaDTO.getHorarioFinal());
            reserva.setFecha(diaDTO.getFecha());
            reserva.setAula(aulaRepository.findById(diaDTO.getAulaId()).orElse(null));
            diasReserva.add(diaReserva);
        }


        return reserva;
    }

    private ReservaPeriodica convertirToReservaPeriodica(ReservaDTO dto) {
        ReservaPeriodica reserva = new ReservaPeriodica();
        reserva.setSolicitante(dto.getSolicitante());
        reserva.setCorreo(dto.getCorreo());
        reserva.setCatedra(dto.getCatedra());
        reserva.setCantidadAlumnos(dto.getCantidadAlumnos());
        reserva.setPeriodo(dto.getPeriodo());

        List<DiaReserva> diasReserva = new ArrayList<>();
        for (DiaReservaDTO diaDTO : dto.getDiasReserva()) {
            DiaReserva diaReserva = new DiaReserva();
            diaReserva.setDiaSemana(DiaSemana.valueOf(diaDTO.getDiaSemana().name()));
            diaReserva.setHorarioInicio(diaDTO.getHorarioInicio());
            diaReserva.setHorarioFinal(diaDTO.getHorarioFinal());
            diaReserva.setAula(aulaRepository.findById(diaDTO.getAulaId()).orElse(null));
            diasReserva.add(diaReserva);
        }
        reserva.setDiasReserva(diasReserva);
        return reserva;
    }

}

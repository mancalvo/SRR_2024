package com.example.Backend.Mapper;

import com.example.Backend.DTO.DiaReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Entity.ReservaPeriodicaDiasReserva;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservaPeriodicaMapperImpl implements ReservaPeriodicaMapper{

    @Autowired
    private AulaRepository aulaRepository;

    @Override
    public ReservaPeriodica convertirToReservaPeriodica(ReservaDTO dto) {
        ReservaPeriodica reserva = new ReservaPeriodica();
        reserva.setSolicitante(dto.getSolicitante());
        reserva.setCorreo(dto.getCorreo());
        reserva.setCatedra(dto.getCatedra());
        reserva.setCantidadAlumnos(dto.getCantidadAlumnos());
        reserva.setTipoReserva(dto.getTipoReserva());
        reserva.setPeriodo(dto.getPeriodo());

        for (DiaReservaDTO diaDTO : dto.getDiasReserva()) {
            ReservaPeriodicaDiasReserva diaReserva = new ReservaPeriodicaDiasReserva();
            diaReserva.setDiaSemana(DiaSemana.valueOf(diaDTO.getDiaSemana().name()));
            diaReserva.setHorarioInicio(diaDTO.getHorarioInicio());
            diaReserva.setHorarioFinal(diaDTO.getHorarioFinal());
            Aula aula = aulaRepository.findById(diaDTO.getAulaId())
                    .orElseThrow(() -> new ReservaDataException("Aula no encontrada con ID: " + diaDTO.getAulaId()));
            diaReserva.setAula(aula);

            // Utilizar el método de asistencia para establecer la relación
            reserva.addDiaReserva(diaReserva);
        }

        return reserva;
    }
}

package com.example.Backend.Mapper;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Aula;
import com.example.Backend.Entity.ReservaEsporadica;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservaEsporadicaMapperImpl implements ReservaEsporadicaMapper{

    @Autowired
    private AulaRepository aulaRepository;

    @Override
    public ReservaEsporadica convertirToReservaEsporadica(ReservaDTO dto) {
        ReservaEsporadica reserva = new ReservaEsporadica();
        reserva.setSolicitante(dto.getSolicitante());
        reserva.setCorreo(dto.getCorreo());
        reserva.setCatedra(dto.getCatedra());
        reserva.setCantidadAlumnos(dto.getCantidadAlumnos());
        reserva.setTipoReserva(dto.getTipoReserva());
        reserva.setFecha(dto.getDiasReserva().get(0).getFecha());
        reserva.setHorarioInicio(dto.getDiasReserva().get(0).getHorarioInicio());
        reserva.setHorarioFinal(dto.getDiasReserva().get(0).getHorarioFinal());

        Long aulaId = dto.getDiasReserva().get(0).getAulaId();
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new ReservaDataException("Aula no encontrada con ID: " + aulaId));
        reserva.setAula(aula);

        return reserva;
    }
}

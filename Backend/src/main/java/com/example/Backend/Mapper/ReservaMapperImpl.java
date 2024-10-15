package com.example.Backend.Mapper;

import com.example.Backend.DTO.DiaReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Repository.AulaRepository;
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
    @Autowired
    private AulaRepository aulaRepository;

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


    private DiaReservaDTO convertirADiaReservaDTO(ReservaPeriodicaDiasReserva diaReserva) {
        DiaReservaDTO dto = new DiaReservaDTO();
        dto.setDiaSemana(diaReserva.getDiaSemana().name());
        dto.setHorarioInicio(diaReserva.getHorarioInicio());
        dto.setHorarioFinal(diaReserva.getHorarioFinal());
        dto.setAulaId(diaReserva.getAula().getId());
        return dto;
    }



    public void actualizarEntidadDesdeDto(ReservaDTO reservaDTO, Reserva reservaExistente) {
        if ("ESPORADICA".equalsIgnoreCase(reservaDTO.getTipoReserva()) && reservaExistente instanceof ReservaEsporadica) {
            actualizarReservaEsporadicaDesdeDto(reservaDTO, (ReservaEsporadica) reservaExistente);
        } else if ("PERIODICA".equalsIgnoreCase(reservaDTO.getTipoReserva()) && reservaExistente instanceof ReservaPeriodica) {
            actualizarReservaPeriodicaDesdeDto(reservaDTO, (ReservaPeriodica) reservaExistente);
        } else {
            throw new ReservaDataException("Tipo de ReservaDTO y Reserva no coinciden para la actualización");
        }
    }

    private void actualizarReservaEsporadicaDesdeDto(ReservaDTO reservaDTO, ReservaEsporadica reservaExistente) {
        reservaExistente.setSolicitante(reservaDTO.getSolicitante());
        reservaExistente.setCorreo(reservaDTO.getCorreo());
        reservaExistente.setCatedra(reservaDTO.getCatedra());
        reservaExistente.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());

        if (reservaDTO.getDiasReserva() != null && !reservaDTO.getDiasReserva().isEmpty()) {
            DiaReservaDTO diaDTO = reservaDTO.getDiasReserva().get(0); // Suponiendo una sola fecha para esporádicas
            reservaExistente.setFecha(diaDTO.getFecha());
            reservaExistente.setHorarioInicio(diaDTO.getHorarioInicio());
            reservaExistente.setHorarioFinal(diaDTO.getHorarioFinal());
            Aula aula = aulaRepository.findById(diaDTO.getAulaId())
                    .orElseThrow(() -> new ReservaDataException("Aula no encontrada con ID: " + diaDTO.getAulaId()));
            reservaExistente.setAula(aula);
        }
    }

    private void actualizarReservaPeriodicaDesdeDto(ReservaDTO reservaDTO, ReservaPeriodica reservaExistente) {
        reservaExistente.setSolicitante(reservaDTO.getSolicitante());
        reservaExistente.setCorreo(reservaDTO.getCorreo());
        reservaExistente.setCatedra(reservaDTO.getCatedra());
        reservaExistente.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
        reservaExistente.setPeriodo(reservaDTO.getPeriodo());

        if (reservaDTO.getDiasReserva() != null && !reservaDTO.getDiasReserva().isEmpty()) {
            // Limpiar los días existentes
            reservaExistente.getDiasReserva().clear();
            // Añadir los nuevos días desde el DTO
            for (DiaReservaDTO diaDTO : reservaDTO.getDiasReserva()) {
                ReservaPeriodicaDiasReserva diaReserva = new ReservaPeriodicaDiasReserva();
                diaReserva.setDiaSemana(DiaSemana.valueOf(diaDTO.getDiaSemana()));
                diaReserva.setHorarioInicio(diaDTO.getHorarioInicio());
                diaReserva.setHorarioFinal(diaDTO.getHorarioFinal());
                // Cargar el Aula desde el repositorio usando el aulaId
                Aula aula = aulaRepository.findById(diaDTO.getAulaId())
                        .orElseThrow(() -> new ReservaDataException("Aula no encontrada con ID: " + diaDTO.getAulaId()));
                diaReserva.setAula(aula);
                diaReserva.setReservaPeriodica(reservaExistente); // Establecer la relación
                reservaExistente.getDiasReserva().add(diaReserva);
            }
        }
    }
}

package com.example.Backend.Mapper;

import com.example.Backend.DTO.DiaReservaDTO;
import com.example.Backend.DTO.ReservaEsporadicaDTO;
import com.example.Backend.DTO.ReservaPeriodicaDTO;
import com.example.Backend.Entity.*;
import com.example.Backend.Enum.DiaSemana;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class ReservaMapper {


    // Mapea ReservaPeriodicaDTO a ReservaPeriodica
    public ReservaPeriodica convertirToEntidad(ReservaPeriodicaDTO dto) {
        ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
        // Otros mapeos

        List<DiaReserva> diasReserva = new ArrayList<>();
        for (DiaReservaDTO diaDTO : dto.getDiasReserva()) {
            DiaReserva diaReserva = new DiaReserva();
            // Suponiendo que DiaSemana es un ENUM con un método que acepta el String correspondiente
            diaReserva.setDiaSemana(diaDTO.getDiaSemana());
            diaReserva.setHorarioInicio(diaDTO.getHorarioInicio());
            diaReserva.setHorarioFinal(diaDTO.getHorarioFinal());
            diasReserva.add(diaReserva);
        }
        reservaPeriodica.setDiasReserva(diasReserva);
        return reservaPeriodica;
    }

    // Mapea ReservaEsporadicaDTO a ReservaEsporadica
    public ReservaEsporadica convertirToEntidad(ReservaEsporadicaDTO dto) {
        ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
        reservaEsporadica.setSolicitante(dto.getSolicitante());
        reservaEsporadica.setCorreo(dto.getCorreo());
        reservaEsporadica.setCatedra(dto.getCatedra());
        reservaEsporadica.setFecha(dto.getFecha());
        reservaEsporadica.setHorarioInicio(dto.getHorarioInicio());
        reservaEsporadica.setHorarioFinal(dto.getHorarioFinal());
        reservaEsporadica.setCantidadAlumnos(dto.getCantidadAlumnos());

        Aula aula = aulaRepository.findById(dto.getAulaId())
                .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada con ID: " + dto.getAulaId()));
        reservaEsporadica.setAula(aula);

        return reservaEsporadica;
    }

    // Mapea ReservaPeriodica a ReservaPeriodicaDTO
    public ReservaPeriodicaDTO convertirToDTO(ReservaPeriodica reservaPeriodica) {
        ReservaPeriodicaDTO dto = new ReservaPeriodicaDTO();
        dto.setSolicitante(reservaPeriodica.getSolicitante());
        dto.setCorreo(reservaPeriodica.getCorreo());
        dto.setCatedra(reservaPeriodica.getCatedra());
        dto.setPeriodo(reservaPeriodica.getPeriodo());
        dto.setCantidadAlumnos(reservaPeriodica.getCantidadAlumnos());

        List<DiaReservaDTO> diasReservaDTO = new ArrayList<>();
        for (DiaReserva diaReserva : reservaPeriodica.getDiasReserva()) {
            DiaReservaDTO diaDTO = new DiaReservaDTO();
            diaDTO.setDiaSemana(DiaSemana.valueOf(String.valueOf(diaReserva.getDiaSemana()))); // Ajustado en el próximo punto
            diaDTO.setHorarioInicio(diaReserva.getHorarioInicio());
            diaDTO.setHorarioFinal(diaReserva.getHorarioFinal());
            diaDTO.setAulaId(diaReserva.getAula().getId());
            diasReservaDTO.add(diaDTO);
        }
        dto.setDiasReserva(diasReservaDTO);

        return dto;
    }

    // Mapea ReservaEsporadica a ReservaEsporadicaDTO
    public ReservaEsporadicaDTO convertirToDTO(ReservaEsporadica reservaEsporadica) {
        ReservaEsporadicaDTO dto = new ReservaEsporadicaDTO();
        dto.setSolicitante(reservaEsporadica.getSolicitante());
        dto.setCorreo(reservaEsporadica.getCorreo());
        dto.setCatedra(reservaEsporadica.getCatedra());
        dto.setFecha(reservaEsporadica.getFecha());
        dto.setHorarioInicio(reservaEsporadica.getHorarioInicio());
        dto.setHorarioFinal(reservaEsporadica.getHorarioFinal());
        dto.setCantidadAlumnos(reservaEsporadica.getCantidadAlumnos());
        dto.setAulaId(reservaEsporadica.getAula().getId());

        return dto;
    }


    public static AulaInformaticaDTO convertirAulaInformaticaToDTO(AulaInformatica aula) {
        // Mapear atributos de AulaInformatica a AulaInformaticaDTO
    }

    public static AulaMultimediosDTO convertirAulaMultimediosToDTO(AulaMultimedios aula) {
        // Mapear atributos de AulaMultimedios a AulaMultimediosDTO
    }

    public static AulaSinRecursosDTO convertirAulaSinRecursosToDTO(AulaSinRecursos aula) {
        // Mapear atributos de AulaSinRecursos a AulaSinRecursosDTO
    }

}

package com.example.Backend.Gestores;


import com.example.Backend.DAO.*;
import com.example.Backend.DTO.AulaDTO;
import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.DTO.ReservaSolapadaDTO;
import com.example.Backend.Entidades.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Enum.Tipo_Periodo;
import com.example.Backend.Exceptions.ReservaException;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import com.example.Backend.Gestores.Externos.Periodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GestorAula {

    @Autowired
    private AulaInformaticaDAO aulaInformaticaDAO;
    @Autowired
    private AulaMultimediosDAO aulaMultimediosDAO;
    @Autowired
    private AulaSinRecursosDAO aulaSinRecursosDAO;
    @Autowired
    private ReservaEsporadicaDAO reservaEsporadicaDAO;
    @Autowired
    private ReservaPeriodicaDAO reservaPeriodicaDAO;
    @Autowired
    private GestorPeriodo gestorPeriodo;

    public List<Aula> buscarAulasDisponibles(AulaDisponibilidadRequestDTO requestDTO) {
        // Lista para almacenar las aulas disponibles
        List<Aula> aulasDisponibles = new ArrayList<>();

        // Buscar aulas según el tipo
        if ("INFORMATICA".equalsIgnoreCase(requestDTO.getTipoAula())) {
            // Buscar Aulas de Informática
            List<AulaInformatica> aulasInformatica = aulaInformaticaDAO.findByCapacidad(requestDTO.getCapacidad());
            aulasDisponibles.addAll(aulasInformatica);
        } else if ("MULTIMEDIOS".equalsIgnoreCase(requestDTO.getTipoAula())) {
            // Buscar Aulas Multimedios
            List<AulaMultimedios> aulasMultimedios = aulaMultimediosDAO.findByCapacidad(requestDTO.getCapacidad());
            aulasDisponibles.addAll(aulasMultimedios);
        } else if ("SINRECURSOS".equalsIgnoreCase(requestDTO.getTipoAula())) {
            // Buscar Aulas sin recursos
            List<AulaSinRecursos> aulasSinRecursos = aulaSinRecursosDAO.findByCapacidad(requestDTO.getCapacidad());
            aulasDisponibles.addAll(aulasSinRecursos);
        } else {
            throw new IllegalArgumentException("El tipo de aula debe ser 'INFORMATICA', 'MULTIMEDIOS' o 'SINRECURSOS'.");
        }

        // Crear un conjunto de aulas ocupadas
        Set<Aula> aulasOcupadas = new HashSet<>();

        // Verificar el tipo de reserva y manejar según corresponda
        if ("ESPORADICA".equalsIgnoreCase(requestDTO.getTipoReserva())) {
            manejarReservaEsporadica(requestDTO, aulasOcupadas);
        } else if ("PERIODICA".equalsIgnoreCase(requestDTO.getTipoReserva())) {
            manejarReservaPeriodica(requestDTO, aulasOcupadas);
        } else {
            throw new IllegalArgumentException("El tipo de reserva debe ser 'ESPORADICA' o 'PERIODICA'.");
        }

        // Filtrar las aulas disponibles (aquellas que no están ocupadas)
        return aulasDisponibles.stream()
                .filter(aula -> !aulasOcupadas.contains(aula))
                .collect(Collectors.toList());
    }

    private void manejarReservaEsporadica(AulaDisponibilidadRequestDTO requestDTO, Set<Aula> aulasOcupadas) {

        if (requestDTO.getFecha() == null || requestDTO.getHoraInicio() == null || requestDTO.getHoraFinal() == null) {
            throw new IllegalArgumentException("Para una reserva ESPORÁDICA, fecha, horaInicio y horaFinal son obligatorios.");
        }


        LocalTime horaInicio = LocalTime.parse(requestDTO.getHoraInicio());
        LocalTime horaFinal = LocalTime.parse(requestDTO.getHoraFinal());
        // Obtener todas las reservas esporádicas que coinciden con la fecha
        List<DiaEsporadica> diasEsporadicos = reservaEsporadicaDAO.findByFecha(requestDTO.getFecha());
        for (DiaEsporadica dia : diasEsporadicos) {
            if (hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadas.add(dia.getAula());
            }
        }
    }

    private void manejarReservaPeriodica(AulaDisponibilidadRequestDTO requestDTO, Set<Aula> aulasOcupadas) {
        if (requestDTO.getTipoPeriodo() == null || requestDTO.getDia() == null || requestDTO.getHoraInicio() == null || requestDTO.getHoraFinal() == null) {
            throw new IllegalArgumentException("Para una reserva PERIÓDICA, tipoPeriodo, día, horaInicio y horaFinal son obligatorios.");
        }

        LocalTime horaInicio = LocalTime.parse(requestDTO.getHoraInicio());
        LocalTime horaFinal = LocalTime.parse(requestDTO.getHoraFinal());
        DiaSemana diaSemana = DiaSemana.valueOf(requestDTO.getDia().toUpperCase());
        Tipo_Periodo tipoPeriodo = Tipo_Periodo.valueOf(requestDTO.getTipoPeriodo().toUpperCase());

        // Obtener el rango de fechas del periodo
        LocalDate fechaInicio = gestorPeriodo.calcularFechaInicio(tipoPeriodo);
        LocalDate fechaFinal = gestorPeriodo.calcularFechaFinal(tipoPeriodo);

        // Obtener todas las reservas periódicas que coinciden con el periodo y el día
        List<DiaPeriodica> diasPeriodicos = reservaPeriodicaDAO.findByDiaSemanaAndFechas(diaSemana, fechaInicio, fechaFinal);
        for (DiaPeriodica dia : diasPeriodicos) {
            if (hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadas.add(dia.getAula());
            }
        }
    }

    private boolean hayConflictoHorario(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return !inicio1.isAfter(fin2) && !fin1.isBefore(inicio2);
    }

}


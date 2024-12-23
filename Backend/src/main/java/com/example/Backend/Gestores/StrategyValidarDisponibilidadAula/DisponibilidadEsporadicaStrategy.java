package com.example.Backend.Gestores.StrategyValidarDisponibilidadAula;

import com.example.Backend.DAO.ReservaEsporadicaDAO;
import com.example.Backend.DAO.ReservaPeriodicaDAO;
import com.example.Backend.DTO.AulaDTO;
import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.DTO.ReservaEsporadicaSolapadaDTO;
import com.example.Backend.DTO.ReservaPeriodicaSolapadaDTO;
import com.example.Backend.DTO.ReservaSolapadaDTO;
import com.example.Backend.Entidades.Aula;
import com.example.Backend.Entidades.DiaEsporadica;
import com.example.Backend.Entidades.DiaPeriodica;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import com.example.Backend.Utils.TimeUtils;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisponibilidadEsporadicaStrategy implements IValidarDisponibilidad {

    @Autowired
    private ReservaEsporadicaDAO reservaEsporadicaDAO;
    @Autowired
    private ReservaPeriodicaDAO reservaPeriodicaDAO;
    @Autowired
    private GestorPeriodo gestorPeriodo;

    @Override
    public AulaDisponibilidadResponseDTO validarDisponibilidad(AulaDisponibilidadRequestDTO requestDTO,
            List<Aula> aulasCompatibles) {

        if (requestDTO.getFecha() == null || requestDTO.getHoraInicio() == null || requestDTO.getHoraFinal() == null) {
            throw new IllegalArgumentException("Para una reserva ESPORÁDICA, fecha, horaInicio y horaFinal son obligatorios.");
        }

        HashMap<Integer, List<DiaEsporadica>> aulasOcupadasEsporadicas = new HashMap<>();
        HashMap<Integer, List<DiaPeriodica>> aulasOcupadasPeriodica = new HashMap<>();

        LocalTime horaInicio = LocalTime.parse(requestDTO.getHoraInicio());
        LocalTime horaFinal = LocalTime.parse(requestDTO.getHoraFinal());
        // Obtener todas las reservas esporádicas que coinciden con la fecha
        List<Integer> aulasIds = aulasCompatibles.stream().map(a -> a.getNumero()).collect(Collectors.toList());
        List<DiaEsporadica> diasEsporadicos = reservaEsporadicaDAO.findDiaEsporadicaByFechaAndAulaIds(requestDTO.getFecha(), aulasIds);
        for (DiaEsporadica dia : diasEsporadicos) {
            if (TimeUtils.hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadasEsporadicas.computeIfAbsent(dia.getAula().getNumero(), k -> new ArrayList<>()).add(dia);
            }
        }

        DiaSemana diaSemana = DiaSemana.valueOf(TimeUtils.convertirDayOfWeekADiaSemana(requestDTO.getFecha().getDayOfWeek()).toString());
        // GESTOR PERIODO -> Obtener periodos para esta fecha
        Integer periodoId = gestorPeriodo.periodoIdQueContieneFecha(requestDTO.getFecha());

        // Obtener todas las reservas periódicas que coinciden con el periodo y el día
        ArrayList<DiaPeriodica> diasPeriodicos = new ArrayList<>(List.of());
        if (periodoId != null) {
            diasPeriodicos.addAll(reservaPeriodicaDAO.findByDiaSemanaAndPeriodoAndAulaIds(diaSemana,
                    periodoId, aulasIds));

            for (DiaPeriodica dia : diasPeriodicos) {
                if (TimeUtils.hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                    aulasOcupadasPeriodica.computeIfAbsent(dia.getAula().getNumero(), k -> new ArrayList<>()).add(dia);
                }
            }
        }

        Set<Integer> aulasOcupadasCombinadas = new HashSet<>(aulasOcupadasEsporadicas.keySet());
        if (periodoId != null) {
            aulasOcupadasCombinadas.addAll(aulasOcupadasPeriodica.keySet());
        }

        ArrayList<Aula> aulasDisponibles = new ArrayList<>();

        for (Aula aula : aulasCompatibles) {
            if (!aulasOcupadasCombinadas.contains(aula.getNumero())) {
                aulasDisponibles.add(aula);
            }
        }

        AulaDisponibilidadResponseDTO responseDTO;

        if (!aulasDisponibles.isEmpty()) {
            List<AulaDTO> aulasDTO = aulasDisponibles.stream().map(a -> a.toDto()).collect(Collectors.toList());
            responseDTO = new AulaDisponibilidadResponseDTO(aulasDTO, null);
        } else {
            responseDTO = generarDTOMenosSuperpuestoEsporadico(aulasCompatibles, horaInicio, horaFinal, aulasOcupadasEsporadicas, aulasOcupadasPeriodica);
        }

        return responseDTO;
    }

    private AulaDisponibilidadResponseDTO generarDTOMenosSuperpuestoEsporadico(
            List<Aula> aulas,
            LocalTime horaInicial,
            LocalTime horaFinal,
            HashMap<Integer, List<DiaEsporadica>> mapaEsporadicas,
            HashMap<Integer, List<DiaPeriodica>> mapaPeriodicas) {

        List<AulaDTO> aulaDTOs = new ArrayList<>();
        List<ReservaSolapadaDTO> reservaSolapadaDTOs = new ArrayList<>();

        for (Aula aula : aulas) {
            List<DiaEsporadica> esporadicas = mapaEsporadicas.getOrDefault(aula.getNumero(), null);
            List<DiaPeriodica> periodicas = mapaPeriodicas.getOrDefault(aula.getNumero(), null);

            ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO(
                    ReservaEsporadicaSolapadaDTO.crearListaReservasEsporadicasSolapadaDTO(esporadicas),
                    ReservaPeriodicaSolapadaDTO.crearListaReservasPeriodicasSolapadaDTO(periodicas)
            );

            AulaDTO aulaDTO = aula.toDto();
            aulaDTOs.add(aulaDTO);
            reservaSolapadaDTOs.add(reservaSolapadaDTO);
        }

        List<Long> valores = IntStream.range(0, aulaDTOs.size())
                .mapToObj(i -> calcularMaximoIntervalo(horaInicial, horaFinal,
                mapaEsporadicas.getOrDefault(aulas.get(i).getNumero(), null),
                mapaPeriodicas.getOrDefault(aulas.get(i).getNumero(), null)))
                .collect(Collectors.toList());

        long maxValue = valores.stream().max(Long::compare).orElse(Long.MIN_VALUE);

        List<Integer> indicesWithMaxValue = IntStream.range(0, valores.size())
                .filter(i -> valores.get(i) == maxValue)
                .boxed()
                .collect(Collectors.toList());

        List<AulaDTO> filteredAulaDTOs = indicesWithMaxValue.stream().map(aulaDTOs::get).collect(Collectors.toList());
        List<ReservaSolapadaDTO> filteredReservaSolapadaDTOs = indicesWithMaxValue.stream().map(reservaSolapadaDTOs::get).collect(Collectors.toList());

        return new AulaDisponibilidadResponseDTO(filteredAulaDTOs, filteredReservaSolapadaDTOs);
    }

    private long calcularMaximoIntervalo(LocalTime horaInicial, LocalTime horaFinal,
            List<DiaEsporadica> esporadicas,
            List<DiaPeriodica> periodicas) {

        List<LocalTime[]> intervalos = new ArrayList<>();

        if (esporadicas != null) {
            for (DiaEsporadica desp : esporadicas) {
                intervalos.add(new LocalTime[]{desp.getHoraInicio(), desp.getHoraFinal()});
            }
        }

        if (periodicas != null) {
            for (DiaPeriodica dper : periodicas) {
                intervalos.add(new LocalTime[]{dper.getHoraInicio(), dper.getHoraFinal()});
            }
        }

        intervalos.sort(Comparator.comparing(intervalo -> intervalo[0]));

        long maximoIntervalo = 0;
        LocalTime ultimoFinal = horaInicial;

        for (LocalTime[] intervalo : intervalos) {
            if (!intervalo[0].isBefore(ultimoFinal)) {
                long tiempoLibre = java.time.Duration.between(ultimoFinal, intervalo[0]).toMinutes();
                maximoIntervalo = Math.max(maximoIntervalo, tiempoLibre);
            }
            ultimoFinal = intervalo[1];
        }

        if (horaFinal.isAfter(ultimoFinal)) {
            long tiempoLibre = java.time.Duration.between(ultimoFinal, horaFinal).toMinutes();
            maximoIntervalo = Math.max(maximoIntervalo, tiempoLibre);
        }

        return maximoIntervalo;

    }

}

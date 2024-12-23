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
import com.example.Backend.Entidades.Periodo;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Enum.Tipo_Periodo;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import com.example.Backend.Utils.TimeUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisponibilidadPeriodicaStrategy implements IValidarDisponibilidad {

    @Autowired
    private ReservaEsporadicaDAO reservaEsporadicaDAO;
    @Autowired
    private ReservaPeriodicaDAO reservaPeriodicaDAO;
    @Autowired
    private GestorPeriodo gestorPeriodo;

    @Override
    public AulaDisponibilidadResponseDTO validarDisponibilidad(AulaDisponibilidadRequestDTO requestDTO,
            List<Aula> aulasCompatibles) {

        HashMap<Integer, List<DiaEsporadica>> aulasOcupadasEsporadicas = new HashMap<>();
        HashMap<Integer, List<DiaPeriodica>> aulasOcupadasPeriodica = new HashMap<>();

        if (requestDTO.getTipoPeriodo() == null || requestDTO.getDia() == null
                || requestDTO.getHoraInicio() == null || requestDTO.getHoraFinal() == null) {
            throw new IllegalArgumentException("Para una reserva PERIODICA, dia, horaInicio y horaFinal son obligatorios.");
        }

        Tipo_Periodo periodoSolicitado = Tipo_Periodo.valueOf(requestDTO.getTipoPeriodo());
        List<Integer> periodosCoincidentes = gestorPeriodo.obtenerPeriodosMasProximoPorTipo(periodoSolicitado);
        DiaSemana diaSemana = DiaSemana.valueOf(requestDTO.getDia());
        List<LocalDate> fechas = TimeUtils.obtenerFechasParaPeriodosYDia(TimeUtils.convertirDiaSemanaADayOfWeek(diaSemana),
                gestorPeriodo.traerPeriodos(periodosCoincidentes));

        LocalTime horaInicio = LocalTime.parse(requestDTO.getHoraInicio());
        LocalTime horaFinal = LocalTime.parse(requestDTO.getHoraFinal());

        // Obtener todas las reservas esporádicas que coinciden con la fecha
        List<Integer> aulasIds = aulasCompatibles.stream().map(a -> a.getNumero()).collect(Collectors.toList());
        List<DiaEsporadica> diasEsporadicos = reservaEsporadicaDAO.findDiaEsporadicaByFechasAndAulaIds(fechas, aulasIds);
        for (DiaEsporadica dia : diasEsporadicos) {
            if (TimeUtils.hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadasEsporadicas.computeIfAbsent(dia.getAula().getNumero(), k -> new ArrayList<>()).add(dia);
            }
        }

        // Obtener todas las reservas periódicas que coinciden con el periodo y el día
        ArrayList<DiaPeriodica> diasPeriodicos = new ArrayList<>(List.of());

        diasPeriodicos.addAll(reservaPeriodicaDAO.findByDiaSemanaAndPeriodosAndAulaIds(diaSemana, periodosCoincidentes, aulasIds));
        for (DiaPeriodica dia : diasPeriodicos) {
            if (TimeUtils.hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadasPeriodica.computeIfAbsent(dia.getAula().getNumero(), k -> new ArrayList<>()).add(dia);
            }
        }

        Set<Integer> aulasOcupadasCombinadas = new HashSet<>(aulasOcupadasEsporadicas.keySet());
        aulasOcupadasCombinadas.addAll(aulasOcupadasPeriodica.keySet());

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
            responseDTO = generarDTOMenosSuperpuestoPeriodico(aulasCompatibles, diaSemana,
                    horaInicio, horaFinal, aulasOcupadasEsporadicas, aulasOcupadasPeriodica, periodoSolicitado);
        }

        return responseDTO;
    }

    private AulaDisponibilidadResponseDTO generarDTOMenosSuperpuestoPeriodico(
            List<Aula> aulas,
            DiaSemana dia,
            LocalTime horaInicial,
            LocalTime horaFinal,
            HashMap<Integer, List<DiaEsporadica>> mapaEsporadicas,
            HashMap<Integer, List<DiaPeriodica>> mapaPeriodicas,
            Tipo_Periodo tipoPeriodo) {

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

        HashMap<Tipo_Periodo, Long> cantidadDiasPorPeriodo = new HashMap<>();
        List<Integer> periodosCoincidentes = gestorPeriodo.obtenerPeriodosMasProximoPorTipo(tipoPeriodo);
        for (Integer pId : periodosCoincidentes) {
            Periodo p = gestorPeriodo.traerPeriodo(pId);
            cantidadDiasPorPeriodo.put(p.getTipoPeriodo(),
                    TimeUtils.cantidadDeDiasEntreFechas(p.getFechaInicio(), p.getFechaFin(), dia));
        }

        long valorMinimo = Long.MAX_VALUE;
        for (int i = 0; i < aulas.size(); i++) {
            long valorActual = calcularMaximasHorasSolapadas(horaInicial, horaFinal,
                    mapaEsporadicas.getOrDefault(aulas.get(i).getNumero(), null),
                    mapaPeriodicas.getOrDefault(aulas.get(i).getNumero(), null),
                    cantidadDiasPorPeriodo, tipoPeriodo);
            valorMinimo = Math.min(valorMinimo, valorActual);
        }

        List<Integer> indicesMinimos = new ArrayList<>();
        for (int i = 0; i < aulas.size(); i++) {
            long valorActual = calcularMaximasHorasSolapadas(horaInicial, horaFinal,
                    mapaEsporadicas.getOrDefault(aulas.get(i).getNumero(), null),
                    mapaPeriodicas.getOrDefault(aulas.get(i).getNumero(), null),
                    cantidadDiasPorPeriodo, tipoPeriodo);
            if (valorActual == valorMinimo) {
                indicesMinimos.add(i);
            }
        }

        List<AulaDTO> aulasDTOMinimas = indicesMinimos.stream()
                .map(aulaDTOs::get)
                .collect(Collectors.toList());
        List<ReservaSolapadaDTO> reservasSolapadasDTOMinimas = indicesMinimos.stream()
                .map(reservaSolapadaDTOs::get)
                .collect(Collectors.toList());

        return new AulaDisponibilidadResponseDTO(aulasDTOMinimas, reservasSolapadasDTOMinimas);
    }

    private long calcularMaximasHorasSolapadas(LocalTime horaInicial, LocalTime horaFinal,
            List<DiaEsporadica> esporadicas,
            List<DiaPeriodica> periodicas,
            HashMap<Tipo_Periodo, Long> cantidadDiasPorPeriodo,
            Tipo_Periodo tipoPeriodo) {

        long minutos = 0;

        if (esporadicas != null) {
            for (DiaEsporadica de : esporadicas) {
                minutos += minutosSolapados(horaInicial, horaFinal, de.getHoraInicio(), de.getHoraFinal());
            }
        }

        if (periodicas != null) {
            for (DiaPeriodica dp : periodicas) {
                long minutosIndividual = minutosSolapados(horaInicial,
                        horaFinal, dp.getHoraInicio(), dp.getHoraFinal());

                if (tipoPeriodo == Tipo_Periodo.PRIMER_CUATRIMESTRE
                        || tipoPeriodo == Tipo_Periodo.ANUAL) {
                    boolean existeEnPrimerCuatrimestre = gestorPeriodo
                            .traerPeriodos(dp.getReserva().getPeriodosId())
                            .stream().anyMatch(p -> p.getTipoPeriodo() == (Tipo_Periodo.PRIMER_CUATRIMESTRE));

                    if (existeEnPrimerCuatrimestre) {
                        minutos
                                += ((cantidadDiasPorPeriodo.get(Tipo_Periodo.PRIMER_CUATRIMESTRE)) * minutosIndividual);
                    }
                }

                if (tipoPeriodo == Tipo_Periodo.SEGUNDO_CUATRIMESTRE
                        || tipoPeriodo == Tipo_Periodo.ANUAL) {

                    boolean existeEnSegundoCuatrimestre = gestorPeriodo
                            .traerPeriodos(dp.getReserva().getPeriodosId())
                            .stream().anyMatch(p -> p.getTipoPeriodo() == (Tipo_Periodo.SEGUNDO_CUATRIMESTRE));

                    if (existeEnSegundoCuatrimestre) {
                        minutos
                                += ((cantidadDiasPorPeriodo.get(Tipo_Periodo.SEGUNDO_CUATRIMESTRE)) * minutosIndividual);
                    }
                }
            }
        }

        return minutos;
    }

    private long minutosSolapados(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {

        if (fin1.isBefore(inicio2) || fin2.isBefore(inicio1)) {
            return 0;
        }

        LocalTime solapadoInicio = inicio1.isAfter(inicio2) ? inicio1 : inicio2;
        LocalTime solapadoFinal = fin1.isBefore(fin2) ? fin1 : fin2;

        // Calculate the difference in minutes
        return ChronoUnit.MINUTES.between(solapadoInicio, solapadoFinal);
    }

}

package com.example.Backend.Gestores;

import com.example.Backend.DAO.*;
import com.example.Backend.DTO.AulaDTO;
import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.DTO.ReservaEsporadicaSolapadaDTO;
import com.example.Backend.DTO.ReservaPeriodicaSolapadaDTO;
import com.example.Backend.DTO.ReservaSolapadaDTO;
import com.example.Backend.Entidades.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Enum.Tipo_Periodo;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public AulaDisponibilidadResponseDTO buscarAulasDisponibles(AulaDisponibilidadRequestDTO requestDTO) {
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

        // Crear conjuntos de aulas ocupadas
        HashMap<Aula, List<DiaEsporadica>> aulasOcupadasEsporadicas = new HashMap<>();
        HashMap<Aula, List<DiaPeriodica>> aulasOcupadasPeriodica = new HashMap<>();

        // Verificar el tipo de reserva y manejar según corresponda
        if ("ESPORADICA".equalsIgnoreCase(requestDTO.getTipoReserva())) {
            return manejarReservaEsporadica(requestDTO, aulasDisponibles,
                    aulasOcupadasEsporadicas, aulasOcupadasPeriodica);
            
        } else if ("PERIODICA".equalsIgnoreCase(requestDTO.getTipoReserva())) {
            return manejarReservaPeriodica(requestDTO, aulasDisponibles,
                    aulasOcupadasEsporadicas, aulasOcupadasPeriodica);
            
        } else {
            throw new IllegalArgumentException("El tipo de reserva debe ser 'ESPORADICA' o 'PERIODICA'.");
        }
        
    }

    private AulaDisponibilidadResponseDTO manejarReservaEsporadica(AulaDisponibilidadRequestDTO requestDTO,
            List<Aula> aulasCompatibles, HashMap<Aula, List<DiaEsporadica>> aulasOcupadasEsporadicas,
            HashMap<Aula, List<DiaPeriodica>> aulasOcupadasPeriodica) {

        if (requestDTO.getFecha() == null || requestDTO.getHoraInicio() == null || requestDTO.getHoraFinal() == null) {
            throw new IllegalArgumentException("Para una reserva ESPORÁDICA, fecha, horaInicio y horaFinal son obligatorios.");
        }

        LocalTime horaInicio = LocalTime.parse(requestDTO.getHoraInicio());
        LocalTime horaFinal = LocalTime.parse(requestDTO.getHoraFinal());
        // Obtener todas las reservas esporádicas que coinciden con la fecha
        List<Integer> aulasIds = aulasCompatibles.stream().map(a -> a.getNumero()).collect(Collectors.toList());
        List<DiaEsporadica> diasEsporadicos = reservaEsporadicaDAO.findDiaEsporadicaByFechaAndAulaIds(requestDTO.getFecha(), aulasIds);
        for (DiaEsporadica dia : diasEsporadicos) {
            if (hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadasEsporadicas.computeIfAbsent(dia.getAula(), k -> new ArrayList<>()).add(dia);
            }
        }

        DiaSemana diaSemana = DiaSemana.valueOf(convertirDayOfWeekADiaSemana(requestDTO.getFecha().getDayOfWeek()).toString());
        // GESTOR PERIODO -> Obtener periodos para esta fecha
        Integer periodoId = gestorPeriodo.periodoIdQueContieneFecha(requestDTO.getFecha());

        // Obtener todas las reservas periódicas que coinciden con el periodo y el día
        ArrayList<DiaPeriodica> diasPeriodicos = new ArrayList<>(List.of());
        if (periodoId != null) {
            diasPeriodicos.addAll(reservaPeriodicaDAO.findByDiaSemanaAndPeriodoAndAulaIds(diaSemana,
                    periodoId, aulasIds));

            for (DiaPeriodica dia : diasPeriodicos) {
                if (hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                    aulasOcupadasPeriodica.computeIfAbsent(dia.getAula(), k -> new ArrayList<>()).add(dia);
                }
            }
        }

        Set<Aula> ocupadasCombinadas = new HashSet<>(aulasOcupadasEsporadicas.keySet());
        if (periodoId != null) {
            ocupadasCombinadas.addAll(aulasOcupadasPeriodica.keySet());
        }

        ArrayList<Aula> aulasDisponibles = new ArrayList<>();

        for (Aula aula : aulasCompatibles) {
            if (!ocupadasCombinadas.contains(aula)) {
                aulasDisponibles.add(aula);
            }
        }

        AulaDisponibilidadResponseDTO responseDTO;

        if (!aulasDisponibles.isEmpty()) {
            List<AulaDTO> aulasDTO = aulasDisponibles.stream().map(a -> aulaToAulaDTO(a)).collect(Collectors.toList());
            responseDTO = new AulaDisponibilidadResponseDTO(aulasDTO, null);
        } else {
            responseDTO = generarDTOMenosSuperpuestoEsporadico(aulasCompatibles, horaInicio, horaFinal, aulasOcupadasEsporadicas, aulasOcupadasPeriodica);
        }

        return responseDTO;
    }

    private AulaDisponibilidadResponseDTO manejarReservaPeriodica(AulaDisponibilidadRequestDTO requestDTO,
            List<Aula> aulasCompatibles, HashMap<Aula, List<DiaEsporadica>> aulasOcupadasEsporadicas,
            HashMap<Aula, List<DiaPeriodica>> aulasOcupadasPeriodica) {

        if (requestDTO.getTipoPeriodo() == null || requestDTO.getDia() == null
                || requestDTO.getHoraInicio() == null || requestDTO.getHoraFinal() == null) {
            throw new IllegalArgumentException("Para una reserva PERIODICA, dia, horaInicio y horaFinal son obligatorios.");
        }

        Tipo_Periodo periodoSolicitado = Tipo_Periodo.valueOf(requestDTO.getTipoPeriodo());
        List<Integer> periodosCoincidentes = gestorPeriodo.obtenerPeriodosMasProximoPorTipo(periodoSolicitado);
        DiaSemana diaSemana = DiaSemana.valueOf(requestDTO.getDia());
        List<LocalDate> fechas = obtenerFechasParaPeriodosYDia(convertirDiaSemanaAWeekOfDay(diaSemana),
                gestorPeriodo.traerPeriodos(periodosCoincidentes));

        LocalTime horaInicio = LocalTime.parse(requestDTO.getHoraInicio());
        LocalTime horaFinal = LocalTime.parse(requestDTO.getHoraFinal());

        // Obtener todas las reservas esporádicas que coinciden con la fecha
        List<Integer> aulasIds = aulasCompatibles.stream().map(a -> a.getNumero()).collect(Collectors.toList());
        List<DiaEsporadica> diasEsporadicos = reservaEsporadicaDAO.findDiaEsporadicaByFechasAndAulaIds(fechas, aulasIds);
        for (DiaEsporadica dia : diasEsporadicos) {
            if (hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadasEsporadicas.computeIfAbsent(dia.getAula(), k -> new ArrayList<>()).add(dia);
            }
        }

        // Obtener todas las reservas periódicas que coinciden con el periodo y el día
        ArrayList<DiaPeriodica> diasPeriodicos = new ArrayList<>(List.of());

        diasPeriodicos.addAll(reservaPeriodicaDAO.findByDiaSemanaAndPeriodosAndAulaIds(diaSemana, periodosCoincidentes, aulasIds));

        for (DiaPeriodica dia : diasPeriodicos) {
            if (hayConflictoHorario(horaInicio, horaFinal, dia.getHoraInicio(), dia.getHoraFinal())) {
                aulasOcupadasPeriodica.computeIfAbsent(dia.getAula(), k -> new ArrayList<>()).add(dia);
            }
        }

        Set<Aula> ocupadasCombinadas = new HashSet<>(aulasOcupadasEsporadicas.keySet());
        ocupadasCombinadas.addAll(aulasOcupadasPeriodica.keySet());

        ArrayList<Aula> aulasDisponibles = new ArrayList<>();

        for (Aula aula : aulasCompatibles) {
            if (!ocupadasCombinadas.contains(aula)) {
                aulasDisponibles.add(aula);
            }
        }

        AulaDisponibilidadResponseDTO responseDTO;

        if (!aulasDisponibles.isEmpty()) {
            List<AulaDTO> aulasDTO = aulasDisponibles.stream().map(a -> aulaToAulaDTO(a)).collect(Collectors.toList());
            responseDTO = new AulaDisponibilidadResponseDTO(aulasDTO, null);
        } else {
            responseDTO = generarDTOMenosSuperpuestoPeriodico(aulasCompatibles, diaSemana,
                    horaInicio, horaFinal, aulasOcupadasEsporadicas, aulasOcupadasPeriodica, periodoSolicitado);
        }

        return responseDTO;
    }

    private AulaDTO aulaToAulaDTO(Aula aula) {
        if (aula == null) {
            return null;
        }
        Integer numero = aula.getNumero();
        int capacidad = aula.getCapacidad();
        String tipoAula;

        if (aula instanceof AulaInformatica) {
            tipoAula = "INFORMATICA";
        } else if (aula instanceof AulaMultimedios) {
            tipoAula = "MULTIMEDIOS";
        } else if (aula instanceof AulaSinRecursos) {
            tipoAula = "SINRECURSOS";
        } else {
            return null;
        }
        return new AulaDTO(numero, capacidad, tipoAula);
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
    
    private long calcularMaximasHorasSolapadas(LocalTime horaInicial, LocalTime horaFinal,
            List<DiaEsporadica> esporadicas,
            List<DiaPeriodica> periodicas,
            HashMap<Tipo_Periodo, Long> cantidadDiasPorPeriodo,
            Tipo_Periodo tipoPeriodo) {
        
        long minutos = 0;
        
        if(esporadicas != null) {
            for (DiaEsporadica de : esporadicas) {
                minutos += minutosSolapados(horaInicial, horaFinal, de.getHoraInicio(), de.getHoraFinal());
            }
        }
        
        if(periodicas != null) {
            for (DiaPeriodica dp : periodicas) {
                long minutosIndividual = minutosSolapados(horaInicial, 
                        horaFinal, dp.getHoraInicio(), dp.getHoraFinal());
                
                if (tipoPeriodo == Tipo_Periodo.PRIMER_CUATRIMESTRE 
                        || tipoPeriodo == Tipo_Periodo.ANUAL) {
                    
                    boolean existeEnPrimerCuatrimestre = gestorPeriodo
                            .traerPeriodos(dp.getReserva().getPeriodosId())
                            .stream().anyMatch(p -> p.getTipoPeriodo().equals(Tipo_Periodo.PRIMER_CUATRIMESTRE));
                    
                    if (existeEnPrimerCuatrimestre) {
                        minutos += 
                                cantidadDiasPorPeriodo.get(Tipo_Periodo.PRIMER_CUATRIMESTRE) * minutosIndividual;
                    }
                }
                
                if (tipoPeriodo == Tipo_Periodo.SEGUNDO_CUATRIMESTRE 
                        || tipoPeriodo == Tipo_Periodo.ANUAL) {
                    
                    boolean existeEnSegundoCuatrimestre = gestorPeriodo
                            .traerPeriodos(dp.getReserva().getPeriodosId())
                            .stream().anyMatch(p -> p.getTipoPeriodo().equals(Tipo_Periodo.SEGUNDO_CUATRIMESTRE));
                    
                    if (existeEnSegundoCuatrimestre) {
                        minutos += 
                                cantidadDiasPorPeriodo.get(Tipo_Periodo.SEGUNDO_CUATRIMESTRE) * minutosIndividual;
                    }
                }
            }
        }
        
        return minutos;
    }

    private DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return DiaSemana.LUNES;
            case TUESDAY:
                return DiaSemana.MARTES;
            case WEDNESDAY:
                return DiaSemana.MIERCOLES;
            case THURSDAY:
                return DiaSemana.JUEVES;
            case FRIDAY:
                return DiaSemana.VIERNES;
            case SATURDAY:
                return DiaSemana.SABADO;
            default:
                throw new IllegalArgumentException("Día de la semana no válido: " + dayOfWeek);
        }
    }

    private DayOfWeek convertirDiaSemanaAWeekOfDay(DiaSemana dia) {
        switch (dia) {
            case LUNES:
                return DayOfWeek.MONDAY;
            case MARTES:
                return DayOfWeek.TUESDAY;
            case MIERCOLES:
                return DayOfWeek.WEDNESDAY;
            case JUEVES:
                return DayOfWeek.THURSDAY;
            case VIERNES:
                return DayOfWeek.FRIDAY;
            case SABADO:
                return DayOfWeek.SATURDAY;
            default:
                throw new IllegalArgumentException("Día de la semana no válido: " + dia);
        }
    }

    private AulaDisponibilidadResponseDTO generarDTOMenosSuperpuestoEsporadico(
            List<Aula> aulas,
            LocalTime horaInicial,
            LocalTime horaFinal,
            HashMap<Aula, List<DiaEsporadica>> mapaEsporadicas,
            HashMap<Aula, List<DiaPeriodica>> mapaPeriodicas) {

        List<AulaDTO> aulaDTOs = new ArrayList<>();
        List<ReservaSolapadaDTO> reservaSolapadaDTOs = new ArrayList<>();

        for (Aula aula : aulas) {
            List<DiaEsporadica> esporadicas = mapaEsporadicas.getOrDefault(aula, null);
            List<DiaPeriodica> periodicas = mapaPeriodicas.getOrDefault(aula, null);

            ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO(
                    crearListaReservasEsporadicasSolapadaDTO(esporadicas),
                    crearListaReservasPeriodicasSolapadaDTO(periodicas)
            );

            AulaDTO aulaDTO = aulaToAulaDTO(aula);
            aulaDTOs.add(aulaDTO);
            reservaSolapadaDTOs.add(reservaSolapadaDTO);
        }

        List<Integer> indicesSorted = IntStream.range(0, aulaDTOs.size())
                .boxed()
                .sorted((i, j) -> {
                    long valor1 = calcularMaximoIntervalo(horaInicial, horaFinal,
                            mapaEsporadicas.getOrDefault(aulas.get(i), null), mapaPeriodicas.getOrDefault(aulas.get(i), null));

                    long valor2 = calcularMaximoIntervalo(horaInicial, horaFinal,
                            mapaEsporadicas.getOrDefault(aulas.get(j), null), mapaPeriodicas.getOrDefault(aulas.get(j), null));

                    return Long.compare(valor2, valor1);
                })
                .collect(Collectors.toList());

        List<AulaDTO> sortedAulaDTOs = indicesSorted.stream().map(aulaDTOs::get).collect(Collectors.toList());
        List<ReservaSolapadaDTO> sortedReservaSolapadaDTOs = indicesSorted.stream().map(reservaSolapadaDTOs::get).collect(Collectors.toList());

        return new AulaDisponibilidadResponseDTO(sortedAulaDTOs, sortedReservaSolapadaDTOs);
    }

    private AulaDisponibilidadResponseDTO generarDTOMenosSuperpuestoPeriodico(
            List<Aula> aulas,
            DiaSemana dia,
            LocalTime horaInicial,
            LocalTime horaFinal,
            HashMap<Aula, List<DiaEsporadica>> mapaEsporadicas,
            HashMap<Aula, List<DiaPeriodica>> mapaPeriodicas,
            Tipo_Periodo tipoPeriodo) {

        List<AulaDTO> aulaDTOs = new ArrayList<>();
        List<ReservaSolapadaDTO> reservaSolapadaDTOs = new ArrayList<>();

        for (Aula aula : aulas) {
            List<DiaEsporadica> esporadicas = mapaEsporadicas.getOrDefault(aula, null);
            List<DiaPeriodica> periodicas = mapaPeriodicas.getOrDefault(aula, null);

            ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO(
                    crearListaReservasEsporadicasSolapadaDTO(esporadicas),
                    crearListaReservasPeriodicasSolapadaDTO(periodicas)
            );

            AulaDTO aulaDTO = aulaToAulaDTO(aula);
            aulaDTOs.add(aulaDTO);
            reservaSolapadaDTOs.add(reservaSolapadaDTO);
        }
        
        HashMap<Tipo_Periodo, Long> cantidadDiasPorPeriodo = new HashMap<>();
        List<Integer> periodosCoincidentes = gestorPeriodo.obtenerPeriodosMasProximoPorTipo(tipoPeriodo);
        for (Integer pId : periodosCoincidentes) {
            Periodo p = gestorPeriodo.traerPeriodo(pId);
            cantidadDiasPorPeriodo.put(p.getTipoPeriodo(), cantidadDeDiasEntreFechas(p.getFechaInicio(), p.getFechaFin(), dia));
        }

        List<Integer> indicesSorted = IntStream.range(0, aulaDTOs.size())
                .boxed()
                .sorted((i, j) -> {
                    long valor1 = calcularMaximasHorasSolapadas(horaInicial, horaFinal,
                            mapaEsporadicas.getOrDefault(aulas.get(i), null), mapaPeriodicas.getOrDefault(aulas.get(i), null),
                                    cantidadDiasPorPeriodo, tipoPeriodo);

                    long valor2 = calcularMaximasHorasSolapadas(horaInicial, horaFinal,
                            mapaEsporadicas.getOrDefault(aulas.get(j), null), mapaPeriodicas.getOrDefault(aulas.get(j), null),
                                    cantidadDiasPorPeriodo, tipoPeriodo);

                    return Long.compare(valor1, valor2);
                })
                .collect(Collectors.toList());

        List<AulaDTO> sortedAulaDTOs = indicesSorted.stream().map(aulaDTOs::get).collect(Collectors.toList());
        List<ReservaSolapadaDTO> sortedReservaSolapadaDTOs = indicesSorted.stream().map(reservaSolapadaDTOs::get).collect(Collectors.toList());

        return new AulaDisponibilidadResponseDTO(sortedAulaDTOs, sortedReservaSolapadaDTOs);
    }

    private List<ReservaPeriodicaSolapadaDTO> crearListaReservasPeriodicasSolapadaDTO(List<DiaPeriodica> periodicas) {
        if (periodicas == null) {
            return null;
        }
        return periodicas.stream()
                .map(per -> new ReservaPeriodicaSolapadaDTO(
                per.getReserva().getSolicitante(),
                per.getReserva().getCatedra(),
                per.getReserva().getCorreo(),
                per.getHoraInicio().toString(),
                per.getHoraFinal().toString()))
                .collect(Collectors.toList());
    }

    private List<ReservaEsporadicaSolapadaDTO> crearListaReservasEsporadicasSolapadaDTO(List<DiaEsporadica> esporadicas) {
        if (esporadicas == null) {
            return null;
        }
        return esporadicas.stream()
                .map(esp -> new ReservaEsporadicaSolapadaDTO(
                esp.getReserva().getSolicitante(),
                esp.getReserva().getCatedra(),
                esp.getReserva().getCorreo(),
                esp.getHoraInicio().toString(),
                esp.getHoraFinal().toString()))
                .collect(Collectors.toList());
    }

    private boolean hayConflictoHorario(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return !inicio1.isAfter(fin2) && !fin1.isBefore(inicio2);
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
    
    private Long cantidadDeDiasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin, DiaSemana diaSemana) {
        DayOfWeek dia = convertirDiaSemanaAWeekOfDay(diaSemana);
        return fechaInicio.datesUntil(fechaFin.plusDays(1))
            .filter(d -> d.getDayOfWeek() == dia)
            .count();
    }
    

    private ArrayList<LocalDate> obtenerFechasParaPeriodosYDia(DayOfWeek dia, List<Periodo> periodos) {
        ArrayList<LocalDate> fechas = new ArrayList<>();

        for (Periodo p : periodos) {
            LocalDate actual = p.getFechaInicio();
            LocalDate fechaFinal = p.getFechaFin();

            while (actual.getDayOfWeek() != dia) {
                actual = actual.plusDays(1);
            }

            while (!actual.isAfter(fechaFinal)) {
                fechas.add(actual);
                actual = actual.plusWeeks(1);
            }
        }

        return fechas;
    }
}

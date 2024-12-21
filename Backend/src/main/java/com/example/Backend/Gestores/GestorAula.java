package com.example.Backend.Gestores;


import com.example.Backend.DAO.*;
import com.example.Backend.DTO.*;
import com.example.Backend.Entidades.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
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

        // Crear un conjunto de aulas ocupadas
        //Set<Aula> aulasOcupadas = new HashSet<>();

        // Verificar el tipo de reserva y manejar según corresponda
        if ("ESPORADICA".equalsIgnoreCase(requestDTO.getTipoReserva())) {
            //manejarReservaEsporadica(requestDTO, aulasOcupadas);
            return manejarReservaEsporadicaConMapa(requestDTO, aulasDisponibles);
        } else if ("PERIODICA".equalsIgnoreCase(requestDTO.getTipoReserva())) {
            //manejarReservaPeriodica(requestDTO, aulasOcupadas);
        } else {
            throw new IllegalArgumentException("El tipo de reserva debe ser 'ESPORADICA' o 'PERIODICA'.");
        }
        return null;
/*
        // Filtrar las aulas disponibles (aquellas que no están ocupadas)
        return aulasDisponibles.stream()
                .filter(aula -> !aulasOcupadas.contains(aula))
                .collect(Collectors.toList());
*/
    }


    private AulaDisponibilidadResponseDTO manejarReservaEsporadicaConMapa(AulaDisponibilidadRequestDTO requestDTO, List<Aula> aulasCompatibles) {

        HashMap<Aula, List<DiaEsporadica>> aulasOcupadasEsporadicas = new HashMap<>();
        HashMap<Aula, List<DiaPeriodica>> aulasOcupadasPeriodica = new HashMap<>();

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
        List<Integer> periodosIds = gestorPeriodo.periodosIdQueContienenFecha(requestDTO.getFecha());


        // Obtener todas las reservas periódicas que coinciden con el periodo y el día
        ArrayList<DiaPeriodica> diasPeriodicos = new ArrayList<>(List.of());

        diasPeriodicos.addAll(reservaPeriodicaDAO.findByDiaSemanaAndPeriodosAndAulaIds(diaSemana, periodosIds, aulasIds));


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
            responseDTO = generarDTOMenosSuperpuesto(aulasCompatibles, horaInicio, horaFinal, aulasOcupadasEsporadicas, aulasOcupadasPeriodica);
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

        if(aula instanceof AulaInformatica) {
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

    private DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return DiaSemana.LUNES;
            case TUESDAY: return DiaSemana.MARTES;
            case WEDNESDAY: return DiaSemana.MIERCOLES;
            case THURSDAY: return DiaSemana.JUEVES;
            case FRIDAY: return DiaSemana.VIERNES;
            case SATURDAY: return DiaSemana.SABADO;
            default: throw new IllegalArgumentException("Día de la semana no válido: " + dayOfWeek);
        }
    }


    private AulaDisponibilidadResponseDTO generarDTOMenosSuperpuesto(
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


    private List<ReservaPeriodicaSolapadaDTO> crearListaReservasPeriodicasSolapadaDTO(List<DiaPeriodica> periodicas) {
        if(periodicas == null) {
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
        if(esporadicas == null) {
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


}


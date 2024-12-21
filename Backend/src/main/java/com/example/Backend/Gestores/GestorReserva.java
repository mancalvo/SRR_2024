package com.example.Backend.Gestores;

import com.example.Backend.DAO.*;
import com.example.Backend.DTO.DetalleReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entidades.*;
import com.example.Backend.Enum.Tipo_Aula;
import com.example.Backend.Enum.Tipo_Periodo;
import com.example.Backend.Enum.Tipo_Usuario;
import com.example.Backend.Exceptions.ReservaException;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class GestorReserva {

    @Autowired
    private ReservaEsporadicaDAO reservaEsporadicaDAO;

    @Autowired
    private ReservaPeriodicaDAO reservaPeriodicaDAO;

    @Autowired
    private BedelDAO bedelDAO;

    @Autowired
    private AulaInformaticaDAO aulaInformaticaDAO;

    @Autowired
    private AulaMultimediosDAO aulaMultimediosDAO;

    @Autowired
    private AulaSinRecursosDAO aulaSinRecursosDAO;

    @Autowired
    private GestorPeriodo gestorPeriodos;


    public Integer crearReserva(ReservaDTO reservaDTO) {
        // mostrarDatos(reservaDTO);
        if (!validarDisponibilidadAula(reservaDTO.getDetalleReserva(), tipoPeriodo(reservaDTO.getPeriodo()))) {
            throw new ReservaException("Aulas no disponibles");
        }

        validarDatos(reservaDTO);

        switch (reservaDTO.getTipoReserva()) {
            case "PERIODICA":
                ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
                reservaPeriodica.setSolicitante(reservaDTO.getSolicitante());
                reservaPeriodica.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
                reservaPeriodica.setCatedra(reservaDTO.getCatedra());
                reservaPeriodica.setFecha(reservaDTO.getFechaRealizada());
                reservaPeriodica.setCorreo(reservaDTO.getCorreo());
                reservaPeriodica.setTipoAula(reservaDTO.getTipoAula());
                Periodo periodo = gestorPeriodos.traerPeriodo(reservaDTO.getPeriodo());
                reservaPeriodica.setTipoPeriodo(periodo.getTipo_periodo());
                Usuario bedel = buscarBedelPorNombreUsuario(reservaDTO.getNombreUsuario());
                reservaPeriodica.setBedel(bedel);

                for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                    DiaPeriodica diaPeriodica = new DiaPeriodica();
                    diaPeriodica.setDiaSemana(detalle.getDiaSemana());
                    diaPeriodica.setHoraInicio(detalle.getHorarioInicio());
                    diaPeriodica.setHoraFinal(detalle.getHorarioFinal());
                    Aula aula = obtenerAula(detalle.getAulaId(), reservaDTO.getTipoAula());
                    diaPeriodica.setAula(aula);
                    diaPeriodica.setReserva(reservaPeriodica); // Vinculamos al reservaPeriodica actual
                    reservaPeriodica.getDiasPeriodica().add(diaPeriodica); // Agregamos el diaPeriodica
                }


                return reservaPeriodicaDAO.save(reservaPeriodica).getIdReservaPeriodica();

            case "ESPORADICA":
                ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
                reservaEsporadica.setSolicitante(reservaDTO.getSolicitante());
                reservaEsporadica.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
                reservaEsporadica.setCatedra(reservaDTO.getCatedra());
                reservaEsporadica.setFecha(reservaDTO.getFechaRealizada());
                reservaEsporadica.setCorreo(reservaDTO.getCorreo());
                reservaEsporadica.setTipoAula(reservaDTO.getTipoAula());

                Usuario bedel1 = buscarBedelPorNombreUsuario(reservaDTO.getNombreUsuario());
                reservaEsporadica.setBedel(bedel1);

                for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                    DiaEsporadica diaEsporadica = new DiaEsporadica();

                    diaEsporadica.setFecha(detalle.getFecha());
                    diaEsporadica.setHoraInicio(detalle.getHorarioInicio());
                    diaEsporadica.setHoraFinal(detalle.getHorarioFinal());
                    Aula aula = obtenerAula(detalle.getAulaId(), reservaDTO.getTipoAula());
                    diaEsporadica.setAula(aula);
                    diaEsporadica.setReserva(reservaEsporadica); // Vinculamos al reservaEsporadica actual
                    reservaEsporadica.getDiasEsporadica().add(diaEsporadica);
                }

                return reservaEsporadicaDAO.save(reservaEsporadica).getIdReservaEsporadica();
            default:
                System.out.println("No es PERIODICA ni ESPORADICA");
                throw new ReservaException("Error al guardar la reserva");
        }
    }

    // ======================================================================================================================
    private Tipo_Periodo tipoPeriodo(Tipo_Periodo tipoPeriodo) {
        if(tipoPeriodo == null){
            return Tipo_Periodo.ESPORADICA;
        }
        return tipoPeriodo;
    }
    public boolean validarDisponibilidadAula(List<DetalleReservaDTO> listaDetalleReserva, Tipo_Periodo tipoReserva) {
        /*
        for (DetalleReservaDTO detalle : listaDetalleReserva) {

            switch (tipoReserva) {
                case ESPORADICA:
                    if (!validarDisponibilidadEsporadica(detalle)) {
                        return false;
                    }
                    break;

                case PERIODICA:
                case PRIMER_CUATRIMESTRE:
                case SEGUNDO_CUATRIMESTRE:
                case ANUAL:
                    if (!validarDisponibilidadPeriodica(detalle)) {
                        return false;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de reserva no válido: " + tipoReserva);
            }
        }

         */
        return true;
    }
    /*
    private boolean validarDisponibilidadEsporadica(DetalleReservaDTO detalle) {
        // Validar si existe una reserva esporádica en la fecha y aula
        if (reservaEsporadicaDAO.existsByDiasEsporadica_FechaAndDiasEsporadica_Aula_Numero(
                detalle.getFecha(), detalle.getAulaId())) {
            return false;
        }

        // Validar si alguna reserva periódica coincide con la fecha de la reserva esporádica
        List<ReservaPeriodica> reservasPeriodicas = reservaPeriodicaDAO
                .findByDiasPeriodica_Aula_Numero(detalle.getAulaId());

        for (ReservaPeriodica reservaPeriodica : reservasPeriodicas) {
            if (esFechaEnRangoYCoincideDia(reservaPeriodica, detalle)) {
                return false;
            }
        }

        return true;
    }

    private boolean validarDisponibilidadPeriodica(DetalleReservaDTO detalle) {
        Periodo periodoActual = gestorPeriodos.obtenerPeriodoActual();
        List<LocalDate> fechasDelPeriodo = calcularFechasPorDia(periodoActual, detalle.getDiaSemana());

        for (LocalDate fecha : fechasDelPeriodo) {
            // Validar si hay reservas esporádicas en alguna fecha generada
            if (reservaEsporadicaDAO.existsByDiasEsporadica_FechaAndDiasEsporadica_Aula_Numero(
                    fecha, detalle.getAulaId())) {
                return false;
            }

            // Validar si hay reservas periódicas que coincidan
            List<ReservaPeriodica> reservasPeriodicas = reservaPeriodicaDAO
                    .findByDiasPeriodica_Aula_Numero(detalle.getAulaId());

            for (ReservaPeriodica reservaPeriodica : reservasPeriodicas) {
                if (esFechaEnRangoYCoincideDia(reservaPeriodica, fecha, detalle)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean esFechaEnRangoYCoincideDia(ReservaPeriodica reserva, DetalleReservaDTO detalle) {

        Periodo periodo = gestorPeriodos.traerPeriodo(reserva.getPeriodo());

        if (periodo == null) {
            throw new IllegalArgumentException("No se encontró el periodo para el tipo: " + reserva.getPeriodo());
        }

        LocalDate fechaInicio = periodo.getFechaInicio();
        LocalDate fechaFin = periodo.getFechaFin();

        return !detalle.getFecha().isBefore(fechaInicio) &&
                !detalle.getFecha().isAfter(fechaFin) &&
                reserva.getDiasPeriodica().stream().anyMatch(dia ->
                        dia.getDiaSemana().equals(detalle.getFecha().getDayOfWeek()) &&
                                horaSeSuperpone(dia.getHoraInicio(), dia.getHoraFinal(), detalle.getHorarioInicio(), detalle.getHorarioFinal())
                );


        return true;
    }


    private boolean esFechaEnRangoYCoincideDia(ReservaPeriodica reserva, LocalDate fecha, DetalleReservaDTO detalle) {
        // Obtener el periodo correspondiente al tipo de periodo
        Periodo periodo = gestorPeriodos.traerPeriodo(reserva.getPeriodo());

        if (periodo == null) {
            throw new IllegalArgumentException("No se encontró el periodo para el tipo: " + reserva.getPeriodo());
        }

        return !fecha.isBefore(periodo.getFechaInicio()) &&
                !fecha.isAfter(periodo.getFechaFin()) &&
                reserva.getDiasPeriodica().stream().anyMatch(dia ->
                        dia.getDiaSemana().equals(fecha.getDayOfWeek()) &&
                                horaSeSuperpone(dia.getHoraInicio(), dia.getHoraFinal(), detalle.getHorarioInicio(), detalle.getHorarioFinal())
                );
    }


    private List<LocalDate> calcularFechasPorDia(Periodo periodo, DiaSemana diaSemana) {
        List<LocalDate> fechas = new ArrayList<>();
        LocalDate fecha = periodo.getFechaInicio();

        DayOfWeek dayOfWeek = convertirADayOfWeek(diaSemana);

        while (!fecha.isAfter(periodo.getFechaFin())) {
            if (fecha.getDayOfWeek().equals(dayOfWeek)) {
                fechas.add(fecha);
            }
            fecha = fecha.plusDays(1);
        }

        return fechas;
    }

    private DayOfWeek convertirADayOfWeek(DiaSemana diaSemana) {
        switch (diaSemana) {
            case LUNES: return DayOfWeek.MONDAY;
            case MARTES: return DayOfWeek.TUESDAY;
            case MIERCOLES: return DayOfWeek.WEDNESDAY;
            case JUEVES: return DayOfWeek.THURSDAY;
            case VIERNES: return DayOfWeek.FRIDAY;
            case SABADO: return DayOfWeek.SATURDAY;
            default: throw new IllegalArgumentException("Día de la semana no válido: " + diaSemana);
        }
    }


    private boolean horaSeSuperpone(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return !(fin1.isBefore(inicio2) || inicio1.isAfter(fin2));
    }

    */
    //========================================================================================================================




    private Usuario buscarBedelPorNombreUsuario(String nombreUsuario) {
        Optional<Usuario> bedelOpt = bedelDAO.findByNombreUsuarioAndTipoUsuarioAndActivo(
                nombreUsuario, Tipo_Usuario.BEDEL, true
        );

        if (bedelOpt.isPresent()) {
            return bedelOpt.get();
        } else {
            throw new ReservaException("Bedel con el nombre de usuario " + nombreUsuario + " no encontrado.");
        }
    }


    public Aula obtenerAula(Integer aulaId, Tipo_Aula tipoAula) {
        switch (tipoAula) {
            case INFORMATICA:
                return aulaInformaticaDAO.findById(aulaId)
                        .orElseThrow(() -> new ReservaException("Aula de informática no encontrada"));

            case MULTIMEDIOS:
                return aulaMultimediosDAO.findById(aulaId)
                        .orElseThrow(() -> new ReservaException("Aula multimedios no encontrada"));

            case SIN_RECURSO:
                return aulaSinRecursosDAO.findById(aulaId)
                        .orElseThrow(() -> new ReservaException("Aula sin recursos no encontrada"));

            default:
                throw new IllegalArgumentException("Tipo de aula no válido");
        }
    }

    public void validarDatos(ReservaDTO reservaDTO) {
        if (reservaDTO == null) {
            throw new ReservaException("La reserva no puede ser nula.");
        }

        // Validar campos básicos
        if (reservaDTO.getNombreUsuario() == null || reservaDTO.getNombreUsuario().isEmpty()) {
            throw new ReservaException("El nombre del usuario no puede estar vacío.");
        }

        if (reservaDTO.getSolicitante() == null || reservaDTO.getSolicitante().isEmpty()) {
            throw new ReservaException("El solicitante no puede estar vacío.");
        }

        if (reservaDTO.getCorreo() == null || !reservaDTO.getCorreo().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ReservaException("El correo electrónico no es válido.");
        }

        if (reservaDTO.getCatedra() == null || reservaDTO.getCatedra().isEmpty()) {
            throw new ReservaException("La cátedra no puede estar vacía.");
        }

        if (reservaDTO.getTipoAula() == null) {
            throw new ReservaException("El tipo de aula no puede ser nulo.");
        }

        if (reservaDTO.getDetalleReserva() == null || reservaDTO.getDetalleReserva().isEmpty()) {
            throw new ReservaException("El detalle de la reserva no puede estar vacío.");
        }

        if (reservaDTO.getCantidadAlumnos() == null || reservaDTO.getCantidadAlumnos() <= 0) {
            throw new ReservaException("La cantidad de alumnos debe ser mayor a 0.");
        }

        if (reservaDTO.getTipoReserva() == null ||
                (!reservaDTO.getTipoReserva().equalsIgnoreCase("PERIODICA") &&
                        !reservaDTO.getTipoReserva().equalsIgnoreCase("ESPORADICA"))) {
            throw new ReservaException("El tipo de reserva debe ser 'Periódica' o 'Esporádica'.");
        }

        // Validar detalle según el tipo de reserva
        switch (reservaDTO.getTipoReserva().toUpperCase()) {
            case "PERIODICA":
                for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                    validarDetalleReservaPeriodica(detalle);
                }
                break;

            case "ESPORADICA":
                for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                    validarDetalleReservaEsporadica(detalle);
                }
                break;

            default:
                throw new ReservaException("Tipo de reserva no válido.");
        }
    }

    private void validarDetalleReservaPeriodica(DetalleReservaDTO detalle) {
        if (detalle.getDiaSemana() == null) {
            throw new ReservaException("El día de la semana no puede ser nulo.");
        }

        if (detalle.getHorarioInicio() == null || detalle.getHorarioFinal() == null) {
            throw new ReservaException("Los horarios de inicio y final no pueden ser nulos.");
        }

        if (detalle.getHorarioInicio().isAfter(detalle.getHorarioFinal())) {
            throw new ReservaException("El horario de inicio no puede ser posterior al horario final.");
        }

        if (detalle.getAulaId() == null || detalle.getAulaId() <= 0) {
            throw new ReservaException("El ID del aula debe ser mayor a 0.");
        }
    }

    private void validarDetalleReservaEsporadica(DetalleReservaDTO detalle) {
        if (detalle.getFecha() == null) {
            throw new ReservaException("La fecha no puede ser nula.");
        }

        // Validar que la fecha no sea domingo
        if (detalle.getFecha().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new ReservaException("No se pueden realizar reservas los domingos.");
        }

        if (detalle.getHorarioInicio() == null || detalle.getHorarioFinal() == null) {
            throw new ReservaException("Los horarios de inicio y final no pueden ser nulos.");
        }

        if (detalle.getHorarioInicio().isAfter(detalle.getHorarioFinal())) {
            throw new ReservaException("El horario de inicio no puede ser posterior al horario final.");
        }

        if (detalle.getAulaId() == null || detalle.getAulaId() <= 0) {
            throw new ReservaException("El ID del aula debe ser mayor a 0.");
        }
    }

    private void mostrarDatos(ReservaDTO reservaDTO) {
        System.out.println("Nombre Usuario: " + reservaDTO.getNombreUsuario());
        System.out.println("Solicitante: " + reservaDTO.getSolicitante());
        System.out.println("Correo: " + reservaDTO.getCorreo());
        System.out.println("Cátedra: " + reservaDTO.getCatedra());
        System.out.println("Fecha Realizada: " + reservaDTO.getFechaRealizada());
        System.out.println("Tipo de Reserva: " + reservaDTO.getTipoReserva());
        System.out.println("Periodo: " + (reservaDTO.getPeriodo() != null ? reservaDTO.getPeriodo() : "N/A"));
        System.out.println("Tipo de Aula: " + reservaDTO.getTipoAula());
        System.out.println("Cantidad de Alumnos: " + reservaDTO.getCantidadAlumnos());

        if (reservaDTO.getDetalleReserva() != null && !reservaDTO.getDetalleReserva().isEmpty()) {
            System.out.println("Detalles de la Reserva:");
            for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                System.out.println("\tDía de la Semana: " + detalle.getDiaSemana());
                System.out.println("\tFecha: " + detalle.getFecha());
                System.out.println("\tHorario Inicio: " + detalle.getHorarioInicio());
                System.out.println("\tHorario Final: " + detalle.getHorarioFinal());
                System.out.println("\tAula ID: " + detalle.getAulaId());
            }
        } else {
            System.out.println("No hay detalles de reserva.");
        }
    }

}



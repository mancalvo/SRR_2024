package com.example.Backend.Gestores;

import com.example.Backend.DAO.*;
import com.example.Backend.DTO.DetalleReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.DTO.ReservaEsporadicaSolapadaDTO;
import com.example.Backend.DTO.ReservaPeriodicaSolapadaDTO;
import com.example.Backend.Entidades.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Enum.Tipo_Aula;
import com.example.Backend.Enum.Tipo_Periodo;
import com.example.Backend.Enum.Tipo_Usuario;
import com.example.Backend.Exceptions.ReservaException;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import com.example.Backend.Gestores.Externos.Periodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        //mostrarDatos(reservaDTO);
        if (!validarDisponibilidadAula(reservaDTO.getDetalleReserva(), reservaDTO.getPeriodo())) {
            throw new ReservaException("Aulas no disponibles");
        }

        System.out.println("Tipo de Reserva recibido: " + reservaDTO.getTipoReserva());

        switch (reservaDTO.getTipoReserva()) {
            case "PERIODICA":
                ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
                reservaPeriodica.setSolicitante(reservaDTO.getSolicitante());
                reservaPeriodica.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
                reservaPeriodica.setCatedra(reservaDTO.getCatedra());
                reservaPeriodica.setFecha(reservaDTO.getFechaRealizada());
                reservaPeriodica.setCorreo(reservaDTO.getCorreo());
                reservaPeriodica.setPeriodosId(gestorPeriodos.obtenerPeriodosMasProximoPorTipo(reservaDTO.getPeriodo()));

                Usuario bedel = buscarBedelPorNombreUsuario(reservaDTO.getNombreUsuario());
                reservaPeriodica.setBedel(bedel);

                for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                    DiaPeriodica diaPeriodica = new DiaPeriodica();
                    diaPeriodica.setDiaSemana(detalle.getDiaSemana());
                    diaPeriodica.setHoraInicio(detalle.getHorarioInicio());
                    diaPeriodica.setHoraFinal(detalle.getHorarioFinal());
                    Aula aula = obtenerAula(detalle.getAulaId(), reservaDTO.getTipoAula());
                    diaPeriodica.setAula(aula);
                    diaPeriodica.setReserva(reservaPeriodica); // Asegúrate de vincular al reservaPeriodica actual
                    reservaPeriodica.getDiasPeriodica().add(diaPeriodica); // Ahora no lanzará NullPointerException
                }


                return reservaPeriodicaDAO.save(reservaPeriodica).getIdReservaPeriodica();

            case "ESPORADICA":
                // Código para reserva esporádica
                ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
                reservaEsporadica.setSolicitante(reservaDTO.getSolicitante());
                reservaEsporadica.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
                reservaEsporadica.setCatedra(reservaDTO.getCatedra());
                reservaEsporadica.setFecha(reservaDTO.getFechaRealizada());
                reservaEsporadica.setCorreo(reservaDTO.getCorreo());

                Usuario bedel1 = buscarBedelPorNombreUsuario(reservaDTO.getNombreUsuario());
                reservaEsporadica.setBedel(bedel1);

                for (DetalleReservaDTO detalle : reservaDTO.getDetalleReserva()) {
                    DiaEsporadica diaEsporadica = new DiaEsporadica();

                    diaEsporadica.setFecha(detalle.getFecha());
                    diaEsporadica.setHoraInicio(detalle.getHorarioInicio());
                    diaEsporadica.setHoraFinal(detalle.getHorarioFinal());
                    Aula aula = obtenerAula(detalle.getAulaId(), reservaDTO.getTipoAula());
                    diaEsporadica.setAula(aula);
                    diaEsporadica.setReserva(reservaEsporadica); // Asegúrate de vincular al reservaEsporadica actual
                    reservaEsporadica.getDiasEsporadica().add(diaEsporadica);
                }

                return reservaEsporadicaDAO.save(reservaEsporadica).getIdReservaEsporadica();

            default:
                System.out.println("No es PERIODICA ni ESPORADICA");
                throw new ReservaException("Error al guardar la reserva");
        }
    }

    // ======================================================================================================================

    public boolean validarDisponibilidadAula(List<DetalleReservaDTO> listaDetalleReserva, Tipo_Periodo tipoReserva) {
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
                    if (!validarDisponibilidadPeriodica(detalle, tipoReserva)) {
                        return false;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de reserva no válido: " + tipoReserva);
            }
        }
        return true;
    }

    private boolean validarDisponibilidadEsporadica(DetalleReservaDTO detalle) {
        // Validar si existe una reserva esporádica en la fecha y aula
        List<DiaEsporadica> reservasPeriodo = reservaEsporadicaDAO
                    .findDiaEsporadicaByFechaAndAulaId(detalle.getFecha(), detalle.getAulaId());
            
            for (DiaEsporadica de : reservasPeriodo) {
                if (hayConflictoHorario(de.getHoraInicio(), de.getHoraFinal(), 
                        detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                    return false;
                }
            }

        Integer periodoId = gestorPeriodos.periodoIdQueContieneFecha(detalle.getFecha());
        DiaSemana diaSemana = DiaSemana.valueOf(convertirDayOfWeekADiaSemana(detalle.getFecha().getDayOfWeek()).toString());
        List<DiaPeriodica> DiasPer = reservaPeriodicaDAO
            .findByDiaSemanaAndPeriodoAndAulaId(diaSemana, periodoId, detalle.getAulaId());

        for (DiaPeriodica dp : DiasPer) {
            if (hayConflictoHorario(dp.getHoraInicio(), dp.getHoraFinal(), 
                    detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                return false;
            }
        }

        return true;
    }

    private boolean validarDisponibilidadPeriodica(DetalleReservaDTO detalle, Tipo_Periodo tipoPeriodo) {
        
        ArrayList<Integer> periodosIds = gestorPeriodos.obtenerPeriodosMasProximoPorTipo(tipoPeriodo);
        List<Periodo> periodos = gestorPeriodos.traerPeriodos(periodosIds);
        
        for(Periodo p : periodos) {
            List<LocalDate> fechasDelPeriodo = calcularFechasPorDia(p, detalle.getDiaSemana());
            
            List<DiaEsporadica> reservasPeriodo = reservaEsporadicaDAO
                    .findDiaEsporadicaByFechasAndAulaId(fechasDelPeriodo, detalle.getAulaId());
            
            for (DiaEsporadica dr : reservasPeriodo) {
                if (hayConflictoHorario(dr.getHoraInicio(), dr.getHoraFinal(), 
                        detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                    return false;
                }
            }
            
        // Validar si hay reservas periódicas que coincidan
            List<DiaPeriodica> DiasPer = reservaPeriodicaDAO
                    .findByDiaSemanaAndPeriodosAndAulaId(detalle.getDiaSemana(), periodosIds, detalle.getAulaId());

            for (DiaPeriodica dp : DiasPer) {
                if (hayConflictoHorario(dp.getHoraInicio(), dp.getHoraFinal(), 
                        detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                    return false;
                }
            }
        }

        return true;
    }


    private List<LocalDate> calcularFechasPorDia(Periodo periodo, DiaSemana diaSemana) {
        List<LocalDate> fechas = new ArrayList<>();
        LocalDate fecha = periodo.getFechaInicio();

        DayOfWeek dayOfWeek = convertirDiaADayOfWeek(diaSemana);

        while (!fecha.isAfter(periodo.getFechaFin())) {
            if (fecha.getDayOfWeek().equals(dayOfWeek)) {
                fechas.add(fecha);
            }
            fecha = fecha.plusDays(1);
        }

        return fechas;
    }

    private DayOfWeek convertirDiaADayOfWeek(DiaSemana diaSemana) {
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
    
    


    private boolean horaSeSuperpone(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return !(fin1.isBefore(inicio2) || inicio1.isAfter(fin2));
    }


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



    private void mostrarDatos(ReservaDTO reservaDTO) {
        // Mostrar los datos principales de ReservaDTO
        System.out.println("Nombre Usuario: " + reservaDTO.getNombreUsuario());
        System.out.println("Solicitante: " + reservaDTO.getSolicitante());
        System.out.println("Correo: " + reservaDTO.getCorreo());
        System.out.println("Cátedra: " + reservaDTO.getCatedra());
        System.out.println("Fecha Realizada: " + reservaDTO.getFechaRealizada());
        System.out.println("Tipo de Reserva: " + reservaDTO.getTipoReserva());
        System.out.println("Periodo: " + (reservaDTO.getPeriodo() != null ? reservaDTO.getPeriodo() : "N/A"));
        System.out.println("Tipo de Aula: " + reservaDTO.getTipoAula());
        System.out.println("Cantidad de Alumnos: " + reservaDTO.getCantidadAlumnos());

        // Mostrar los detalles de cada reserva en la lista detalleReserva
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

    private boolean hayConflictoHorario(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return !inicio1.isAfter(fin2) && !fin1.isBefore(inicio2);
    }
    
    
}



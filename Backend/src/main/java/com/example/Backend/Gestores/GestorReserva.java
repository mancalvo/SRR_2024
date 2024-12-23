package com.example.Backend.Gestores;

import com.example.Backend.Utils.TimeUtils;
import com.example.Backend.DAO.*;
import com.example.Backend.DTO.DetalleReservaDTO;
import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entidades.*;
import com.example.Backend.Enum.DiaSemana;
import com.example.Backend.Enum.Tipo_Aula;
import com.example.Backend.Enum.Tipo_Periodo;
import com.example.Backend.Enum.Tipo_Usuario;
import com.example.Backend.Exceptions.ReservaException;
import com.example.Backend.Gestores.Externos.GestorPeriodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
        if (!confirmarDisponibilidadAula(reservaDTO.getDetalleReserva(), reservaDTO.getPeriodo())) {
            throw new ReservaException("Aulas no disponibles");
        }

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
                    diaPeriodica.setReserva(reservaPeriodica); // Vinculamos al reservaPeriodica actual
                    reservaPeriodica.getDiasPeriodica().add(diaPeriodica); // Lo agregamos a la lista
                }


                return reservaPeriodicaDAO.save(reservaPeriodica).getIdReservaPeriodica();

            case "ESPORADICA":
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
                    diaEsporadica.setReserva(reservaEsporadica); // Vinculamos al reservaEsporadica actual
                    reservaEsporadica.getDiasEsporadica().add(diaEsporadica); // Lo agregamos a la lista
                }

                return reservaEsporadicaDAO.save(reservaEsporadica).getIdReservaEsporadica();

            default:
                throw new ReservaException("Error al guardar la reserva");
        }
    }


    public boolean confirmarDisponibilidadAula(List<DetalleReservaDTO> listaDetalleReserva, Tipo_Periodo tipoReserva) {
        for (DetalleReservaDTO detalle : listaDetalleReserva) {

            switch (tipoReserva) {
                case ESPORADICA:
                    if (!confirmarDisponibilidadEsporadica(detalle)) {
                        return false;
                    }
                    break;

                case PERIODICA:
                case PRIMER_CUATRIMESTRE:
                case SEGUNDO_CUATRIMESTRE:
                case ANUAL:
                    if (!confirmarDisponibilidadPeriodica(detalle, tipoReserva)) {
                        return false;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de reserva no válido: " + tipoReserva);
            }
        }
        return true;
    }

    private boolean confirmarDisponibilidadEsporadica(DetalleReservaDTO detalle) {
        // Validar si existe una reserva esporádica en la fecha y aula
        List<DiaEsporadica> reservasPeriodo = reservaEsporadicaDAO
                    .findDiaEsporadicaByFechaAndAulaId(detalle.getFecha(), detalle.getAulaId());
            
            for (DiaEsporadica de : reservasPeriodo) {
                if (TimeUtils.hayConflictoHorario(de.getHoraInicio(), de.getHoraFinal(), 
                        detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                    return false;
                }
            }

        Integer periodoId = gestorPeriodos.periodoIdQueContieneFecha(detalle.getFecha());
        DiaSemana diaSemana = DiaSemana.valueOf(TimeUtils.convertirDayOfWeekADiaSemana(detalle.getFecha().getDayOfWeek()).toString());
        List<DiaPeriodica> DiasPer = reservaPeriodicaDAO
            .findByDiaSemanaAndPeriodoAndAulaId(diaSemana, periodoId, detalle.getAulaId());

        for (DiaPeriodica dp : DiasPer) {
            if (TimeUtils.hayConflictoHorario(dp.getHoraInicio(), dp.getHoraFinal(), 
                    detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                return false;
            }
        }

        return true;
    }

    private boolean confirmarDisponibilidadPeriodica(DetalleReservaDTO detalle, Tipo_Periodo tipoPeriodo) {
        
        ArrayList<Integer> periodosIds = gestorPeriodos.obtenerPeriodosMasProximoPorTipo(tipoPeriodo);
        List<Periodo> periodos = gestorPeriodos.traerPeriodos(periodosIds);
        
        for(Periodo p : periodos) {
            List<LocalDate> fechasDelPeriodo = TimeUtils.obtenerFechasParaPeriodosYDia(
                    TimeUtils.convertirDiaSemanaADayOfWeek(detalle.getDiaSemana()), p);
            
            List<DiaEsporadica> reservasPeriodo = reservaEsporadicaDAO
                    .findDiaEsporadicaByFechasAndAulaId(fechasDelPeriodo, detalle.getAulaId());
            
            for (DiaEsporadica dr : reservasPeriodo) {
                if (TimeUtils.hayConflictoHorario(dr.getHoraInicio(), dr.getHoraFinal(), 
                        detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                    return false;
                }
            }
            
        // Validar si hay reservas periódicas que coincidan
            List<DiaPeriodica> DiasPer = reservaPeriodicaDAO
                    .findByDiaSemanaAndPeriodosAndAulaId(detalle.getDiaSemana(), periodosIds, detalle.getAulaId());

            for (DiaPeriodica dp : DiasPer) {
                if (TimeUtils.hayConflictoHorario(dp.getHoraInicio(), dp.getHoraFinal(), 
                        detalle.getHorarioInicio(), detalle.getHorarioFinal())) {
                    return false;
                }
            }
        }

        return true;
    }



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

}



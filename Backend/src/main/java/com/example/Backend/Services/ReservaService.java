package com.example.Backend.Services;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Entity.ReservaEsporadica;
import com.example.Backend.Entity.ReservaPeriodica;
import com.example.Backend.Exceptions.ReservaDataException;
import com.example.Backend.Mapper.*;
import com.example.Backend.Repository.ReservaRepository;
import com.example.Backend.Strategy.ReservaContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaContext reservaContext;
    private final ReservaMapperImpl reservaMapper;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository,
                          ReservaContext reservaContext,
                          ReservaMapper reservaMapper,
                          ReservaEsporadicaMapper reservaEsporadicaMapper,
                          ReservaPeriodicaMapper reservaPeriodicaMapper, ReservaMapperImpl reservaMapper1) {
        this.reservaRepository = reservaRepository;
        this.reservaContext = reservaContext;
        this.reservaMapper = reservaMapper1;
    }

    @Transactional
    public void procesarReserva(ReservaDTO reservaDTO) {
        Reserva reserva = reservaMapper.convertirToEntidad(reservaDTO);
        reservaContext.procesarReserva(reserva);
    }

    public List<ReservaDTO> obtenerTodasLasReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream()
                .map(reserva -> {
                    // Usamos el mapper para convertir la reserva a ReservaDTO
                    return convertirAReservaDTO(reserva);
                })
                .collect(Collectors.toList());
    }

    public ReservaDTO buscarReservaPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaDataException("Reserva no encontrada para el id: " + id));
        return convertirAReservaDTO(reserva);
    }


    @Transactional
    public void actualizarReserva(Long id, ReservaDTO reservaDTO) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaDataException("Reserva no encontrada para el id: " + id));

        String tipoReservaExistente = reservaExistente.getTipoReserva();
        String tipoReservaNueva = reservaDTO.getTipoReserva().toUpperCase();

        if (!tipoReservaExistente.equalsIgnoreCase(tipoReservaNueva)) {
            reservaRepository.delete(reservaExistente);
            Reserva nuevaReserva = reservaMapper.convertirToEntidad(reservaDTO);
            nuevaReserva.setId(id);
            reservaContext.procesarReserva(nuevaReserva);
        } else {
            reservaMapper.actualizarEntidadDesdeDto(reservaDTO, reservaExistente);
            reservaContext.procesarReserva(reservaExistente);
        }
    }

    @Transactional
    public void eliminarReserva(Long id) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaDataException("Reserva no encontrada para el id: " + id));

        reservaRepository.delete(reservaExistente);

    }

    private ReservaDTO convertirAReservaDTO(Reserva reserva) {
        if (reserva instanceof ReservaPeriodica) {
            return reservaMapper.convertirToReservaPeriodicaDTO((ReservaPeriodica) reserva);
        } else if (reserva instanceof ReservaEsporadica) {
            return reservaMapper.convertirToReservaEsporadicaDTO((ReservaEsporadica) reserva);
        } else {
            throw new ReservaDataException("Tipo de reserva no reconocido: " + reserva.getClass().getSimpleName());
        }
    }
}

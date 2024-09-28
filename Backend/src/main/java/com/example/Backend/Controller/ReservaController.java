package com.example.Backend.Controller;

import com.example.Backend.DTO.ReservaEsporadicaDTO;
import com.example.Backend.Entity.Reserva;
import com.example.Backend.Services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    /**
     * Endpoint para crear una reserva periódica.
     *
     * @param reservaPeriodicaDTO DTO de la reserva periódica.
     * @return Respuesta de éxito o error.
     */
    @PostMapping("/periodica")
    public ResponseEntity<String> crearReservaPeriodica(@RequestBody ReservaPeriodicaDTO reservaPeriodicaDTO) {
        try {
            // Convertir DTO a entidad
            Reserva reserva = reservaService.convertirToEntidad(reservaPeriodicaDTO);
            // Procesar la reserva
            reservaService.procesarReserva(reserva);
            return ResponseEntity.ok("Reserva periódica procesada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la reserva periódica: " + e.getMessage());
        }
    }

    /**
     * Endpoint para crear una reserva esporádica.
     *
     * @param reservaEsporadicaDTO DTO de la reserva esporádica.
     * @return Respuesta de éxito o error.
     */
    @PostMapping("/esporadica")
    public ResponseEntity<String> crearReservaEsporadica(@RequestBody ReservaEsporadicaDTO reservaEsporadicaDTO) {
        try {
            // Convertir DTO a entidad
            Reserva reserva = reservaService.convertirToEntidad(reservaEsporadicaDTO);
            // Procesar la reserva
            reservaService.procesarReserva(reserva);
            return ResponseEntity.ok("Reserva esporádica procesada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la reserva esporádica: " + e.getMessage());
        }
    }

    // Opcional: Endpoints para obtener reservas como DTOs
    /*
    @GetMapping("/periodica/{id}")
    public ResponseEntity<ReservaPeriodicaDTO> obtenerReservaPeriodica(@PathVariable Long id) {
        ReservaPeriodicaDTO dto = reservaService.obtenerReservaPeriodicaDTO(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/esporadica/{id}")
    public ResponseEntity<ReservaEsporadicaDTO> obtenerReservaEsporadica(@PathVariable Long id) {
        ReservaEsporadicaDTO dto = reservaService.obtenerReservaEsporadicaDTO(id);
        return ResponseEntity.ok(dto);
    }
    */

}


package com.example.Backend.Controller;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<String> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        reservaService.procesarReserva(reservaDTO);
        return ResponseEntity.ok("Reserva procesada exitosamente");
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> obtenerReservas() {
        List<ReservaDTO> reservas = reservaService.obtenerTodasLasReservas();
        return ResponseEntity.ok(reservas);
    }

}

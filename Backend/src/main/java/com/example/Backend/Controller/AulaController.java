package com.example.Backend.Controller;

import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.Gestores.GestorAula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/aulas")
@CrossOrigin(origins = "http://localhost:3000")
public class AulaController {

    @Autowired
    private GestorAula gestorAula;

    @GetMapping("/disponibles")
    public ResponseEntity<AulaDisponibilidadResponseDTO> obtenerAulasDisponibles(
            @RequestParam String tipoAula,
            @RequestParam int capacidad,
            @RequestParam String tipoReserva,
            @RequestParam(required = false) String tipoPeriodo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam(required = false) String dia,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") String horaInicio,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") String horaFinal) {
        
        AulaDisponibilidadRequestDTO request = new AulaDisponibilidadRequestDTO(tipoAula,capacidad,tipoReserva,tipoPeriodo,fecha,dia,horaInicio,horaFinal);
        AulaDisponibilidadResponseDTO response =  gestorAula.buscarAulasDisponibles(request);
        return ResponseEntity.ok(response);
    }

}

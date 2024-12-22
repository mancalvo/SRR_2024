package com.example.Backend.Controller;

import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.Entidades.Aula;
import com.example.Backend.Gestores.GestorAula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aulas")
public class AulaController {

    @Autowired
    private GestorAula gestorAula;

    @GetMapping("/disponibles")
    public ResponseEntity<AulaDisponibilidadResponseDTO> obtenerAulasDisponibles(@RequestBody AulaDisponibilidadRequestDTO request) {
        AulaDisponibilidadResponseDTO response =  gestorAula.buscarAulasDisponibles(request);
        return ResponseEntity.ok(response);
    }

}

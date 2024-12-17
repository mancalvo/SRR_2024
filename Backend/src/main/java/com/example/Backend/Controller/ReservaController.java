package com.example.Backend.Controller;

import com.example.Backend.DTO.ReservaDTO;
import com.example.Backend.Gestores.GestorReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private GestorReserva gestorReserva;


    @PostMapping
    public ResponseEntity<String> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        Integer idReserva =  gestorReserva.crearReserva(reservaDTO);
        return new ResponseEntity<>("Reserva guardada correctamente con id: " + idReserva, HttpStatus.CREATED);
    }


}
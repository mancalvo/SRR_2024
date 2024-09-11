package com.example.Backend.Controller;

import com.example.Backend.Entity.Reserva;
import com.example.Backend.Services.IReservaServices;
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
    private IReservaServices reservaServices;

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) {
        reservaServices.create(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    // Actualizar una reserva existente
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody Reserva updatedReserva) {
        updatedReserva.setId(id); // Asegúrate de que la reserva tenga el ID correcto
        reservaServices.update(updatedReserva);
        return ResponseEntity.ok(updatedReserva);
    }

    // Eliminar una reserva por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        reservaServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener una reserva por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Long id) {
        Optional<Reserva> optionalReserva = reservaServices.findById(id);
        return optionalReserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Listar todas las reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        List<Reserva> reservas = reservaServices.findAll();
        return ResponseEntity.ok(reservas);
    }
}


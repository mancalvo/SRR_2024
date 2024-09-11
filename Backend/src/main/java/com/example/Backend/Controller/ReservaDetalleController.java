package com.example.Backend.Controller;

import com.example.Backend.Entity.ReservaDetalle;
import com.example.Backend.Services.IReservaDetalleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reserva-detalles")
public class ReservaDetalleController {

    @Autowired
    private IReservaDetalleServices reservaDetalleServices;

    @PostMapping
    public ResponseEntity<ReservaDetalle> create(@RequestBody ReservaDetalle reservaDetalle) {
        reservaDetalleServices.create(reservaDetalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaDetalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDetalle> update(@PathVariable Long id, @RequestBody ReservaDetalle reservaDetalle) {
        Optional<ReservaDetalle> existingReservaDetalle = reservaDetalleServices.findById(id);
        if (existingReservaDetalle.isPresent()) {
            reservaDetalle.setId(id);
            reservaDetalleServices.update(reservaDetalle);
            return ResponseEntity.ok(reservaDetalle);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaDetalleServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDetalle> findById(@PathVariable Long id) {
        Optional<ReservaDetalle> reservaDetalle = reservaDetalleServices.findById(id);
        return reservaDetalle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ReservaDetalle>> findAll() {
        List<ReservaDetalle> reservaDetalles = reservaDetalleServices.findAll();
        return ResponseEntity.ok(reservaDetalles);
    }
}
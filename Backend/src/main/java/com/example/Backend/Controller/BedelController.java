package com.example.Backend.Controller;

import com.example.Backend.Entity.Bedel;
import com.example.Backend.Services.IBedelServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bedeles")
public class BedelController {

    @Autowired
    private IBedelServices bedelServices;

    @PostMapping
    public ResponseEntity<Bedel> create(@RequestBody Bedel bedel) {
        bedelServices.create(bedel);
        return ResponseEntity.status(HttpStatus.CREATED).body(bedel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bedel> update(@PathVariable Long id, @RequestBody Bedel bedel) {
        Optional<Bedel> existingBedel = bedelServices.findById(id);
        if (existingBedel.isPresent()) {
            bedel.setId(id);
            bedelServices.update(bedel);
            return ResponseEntity.ok(bedel);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bedelServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bedel> findById(@PathVariable Long id) {
        Optional<Bedel> bedel = bedelServices.findById(id);
        return bedel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Bedel>> findAll() {
        List<Bedel> bedeles = bedelServices.findAll();
        return ResponseEntity.ok(bedeles);
    }
}
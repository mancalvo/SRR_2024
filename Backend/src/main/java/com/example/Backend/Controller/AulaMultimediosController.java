package com.example.Backend.Controller;

import com.example.Backend.Entity.AulaMultimedios;
import com.example.Backend.Services.IAulaMultimediosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aulas-multimedios")
public class AulaMultimediosController {

    @Autowired
    private IAulaMultimediosServices aulaMultimediosService;

    @PostMapping
    public ResponseEntity<AulaMultimedios> create(@RequestBody AulaMultimedios aulaMultimedios) {
        aulaMultimediosService.create(aulaMultimedios);
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaMultimedios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaMultimedios> getById(@PathVariable Long id) {
        Optional<AulaMultimedios> aula = aulaMultimediosService.findById(id);
        return aula.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AulaMultimedios>> getAll() {
        List<AulaMultimedios> aulas = aulaMultimediosService.findAll();
        return ResponseEntity.ok(aulas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaMultimedios> update(@PathVariable Long id, @RequestBody AulaMultimedios aulaMultimedios) {
        if (!aulaMultimediosService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        aulaMultimedios.setId(id);
        aulaMultimediosService.update(aulaMultimedios);
        return ResponseEntity.ok(aulaMultimedios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!aulaMultimediosService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        aulaMultimediosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


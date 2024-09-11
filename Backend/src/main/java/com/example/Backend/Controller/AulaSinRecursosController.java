package com.example.Backend.Controller;

import com.example.Backend.Entity.AulaSinRecursos;
import com.example.Backend.Services.IAulaSinRecursosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aulas-sin-recursos")
public class AulaSinRecursosController {

    @Autowired
    private IAulaSinRecursosServices aulaSinRecursosServices;

    @PostMapping
    public ResponseEntity<AulaSinRecursos> create(@RequestBody AulaSinRecursos aulaSinRecursos) {
        aulaSinRecursosServices.create(aulaSinRecursos);
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaSinRecursos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaSinRecursos> update(@PathVariable Long id, @RequestBody AulaSinRecursos aulaSinRecursos) {
        Optional<AulaSinRecursos> existingAula = aulaSinRecursosServices.findById(id);
        if (existingAula.isPresent()) {
            aulaSinRecursos.setId(id);
            aulaSinRecursosServices.update(aulaSinRecursos);
            return ResponseEntity.ok(aulaSinRecursos);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        aulaSinRecursosServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaSinRecursos> findById(@PathVariable Long id) {
        Optional<AulaSinRecursos> aulaSinRecursos = aulaSinRecursosServices.findById(id);
        return aulaSinRecursos.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AulaSinRecursos>> findAll() {
        List<AulaSinRecursos> aulasSinRecursos = aulaSinRecursosServices.findAll();
        return ResponseEntity.ok(aulasSinRecursos);
    }

}

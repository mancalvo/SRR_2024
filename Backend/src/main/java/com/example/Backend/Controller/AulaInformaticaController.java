package com.example.Backend.Controller;

import com.example.Backend.Entity.AulaInformatica;
import com.example.Backend.Services.AulaInformaticaServices;
import com.example.Backend.Services.IAulaInformaticaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aulas-informatica")
public class AulaInformaticaController {

    @Autowired
    private IAulaInformaticaServices aulaInformaticaServices;

    @PostMapping
    public ResponseEntity<AulaInformatica> create(@RequestBody AulaInformatica aulaInformatica) {
        aulaInformaticaServices.create(aulaInformatica);
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaInformatica);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaInformatica> update(@PathVariable Long id, @RequestBody AulaInformatica aulaInformatica) {
        Optional<AulaInformatica> existingAula = aulaInformaticaServices.findById(id);
        if (existingAula.isPresent()) {
            aulaInformatica.setId(id);
            aulaInformaticaServices.update(aulaInformatica);
            return ResponseEntity.ok(aulaInformatica);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        aulaInformaticaServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaInformatica> findById(@PathVariable Long id) {
        Optional<AulaInformatica> aulaInformatica = aulaInformaticaServices.findById(id);
        return aulaInformatica.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AulaInformatica>> findAll() {
        List<AulaInformatica> aulasInformatica = aulaInformaticaServices.findAll();
        return ResponseEntity.ok(aulasInformatica);
    }

}

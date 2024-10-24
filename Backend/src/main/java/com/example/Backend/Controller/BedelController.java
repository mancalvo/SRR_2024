package com.example.Backend.Controller;

import com.example.Backend.DTO.BedelDTO;
import com.example.Backend.Services.IBedelServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/bedeles")
public class BedelController {

    private final IBedelServices bedelServices;

    public BedelController(IBedelServices bedelServices) {
        this.bedelServices = bedelServices;
    }

    @PostMapping
    public ResponseEntity<BedelDTO> create(@RequestBody BedelDTO bedel) {
        BedelDTO createdBedel = bedelServices.create(bedel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBedel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BedelDTO> update(@PathVariable Long id, @RequestBody BedelDTO bedelDto) {
        BedelDTO updatedBedel = bedelServices.update(id, bedelDto);
        return ResponseEntity.ok(updatedBedel);
    }

    @PutMapping("/active")
    public ResponseEntity<String> activarBedel(@RequestBody BedelDTO bedelDto) {
        BedelDTO updatedBedel = bedelServices.activarBedel(bedelDto);
        return ResponseEntity.ok("Bedel activado correctamente");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bedelServices.deleteById(id);
        return ResponseEntity.ok("Bedel eliminado correctamente");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedelDTO> findById(@PathVariable Long id) {
        BedelDTO bedel = bedelServices.findById(id);
        return ResponseEntity.ok(bedel);
    }


    @GetMapping
    public ResponseEntity<List<BedelDTO>> findAll() {
        List<BedelDTO> bedeles = bedelServices.findAll();
        return bedeles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bedeles);
    }
}
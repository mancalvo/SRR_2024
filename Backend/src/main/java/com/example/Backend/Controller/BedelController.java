package com.example.Backend.Controller;

import com.example.Backend.DTO.BedelDTORequest;
import com.example.Backend.DTO.BedelDTOResponse;
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
    public ResponseEntity<BedelDTOResponse> create(@RequestBody BedelDTORequest bedel) {
        BedelDTOResponse createdBedel = bedelServices.create(bedel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBedel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BedelDTOResponse> update(@PathVariable Long id, @RequestBody BedelDTORequest bedelDtoRequest) {
        BedelDTOResponse updatedBedel = bedelServices.update(id, bedelDtoRequest);
        return ResponseEntity.ok(updatedBedel);
    }

    @PutMapping("/active")
    public ResponseEntity<String> activarBedel(@RequestBody BedelDTORequest bedelDtoRequest) {
        bedelServices.activarBedel(bedelDtoRequest);
        return ResponseEntity.ok("Bedel activado correctamente");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bedelServices.deleteById(id);
        return ResponseEntity.ok("Bedel eliminado correctamente");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedelDTOResponse> findById(@PathVariable Long id) {
        BedelDTOResponse bedel = bedelServices.findById(id);
        return ResponseEntity.ok(bedel);
    }


    @GetMapping
    public ResponseEntity<List<BedelDTOResponse>> findAll() {
        List<BedelDTOResponse> bedeles = bedelServices.findAll();
        return bedeles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bedeles);
    }
}
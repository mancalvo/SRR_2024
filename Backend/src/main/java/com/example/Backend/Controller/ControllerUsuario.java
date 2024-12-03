package com.example.Backend.Controller;

import com.example.Backend.DTO.BedelDTO;
import com.example.Backend.DTO.UsuarioDTO;
import com.example.Backend.Gestores.GestorAdministrador;
import com.example.Backend.Gestores.GestorBedel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class ControllerUsuario {

    @Autowired
    private GestorAdministrador gestorAdministrador;

    @Autowired
    private GestorBedel gestorBedel;

    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Integer idAdmin = gestorAdministrador.registrarNuevoUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioDTO.getTipoUsuario() + " creado correctamente con el id: " + idAdmin);
    }

    @GetMapping("/bedel/{id}")
    public ResponseEntity<UsuarioDTO> buscarBedel(@PathVariable Integer id) {
        UsuarioDTO usuario = gestorBedel.buscarBedelPorId(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usuario);
    }

    @GetMapping("/bedels")
    public ResponseEntity<List<BedelDTO>> obtenerTodosLosBedels() {
        List<BedelDTO> bedels = gestorBedel.obtenerTodosLosBedels();
        return ResponseEntity.ok(bedels);
    }


    @PutMapping("/bedel/{id}")
    public ResponseEntity<String> modificarBedel(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) {
        gestorBedel.actualizarBedel(id, usuarioDTO);
        return ResponseEntity.ok("Bedel con ID " + id + " actualizado correctamente.");
    }


    @DeleteMapping("/bedel/{id}")
    public ResponseEntity<String> eliminarBedel(@PathVariable Integer id) {
        gestorBedel.eliminarBedel(id);
        return ResponseEntity.ok("Bedel con ID " + id + " eliminado correctamente.");
    }
}


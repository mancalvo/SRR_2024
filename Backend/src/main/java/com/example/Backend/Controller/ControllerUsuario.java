package com.example.Backend.Controller;

import com.example.Backend.DTO.UsuarioDTO;
import com.example.Backend.Exceptions.BedelException;
import com.example.Backend.Gestores.GestorAdministrador;
import com.example.Backend.Gestores.GestorBedel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {

    private final GestorAdministrador gestorAdministrador;
    private final GestorBedel gestorBedel;

    @Autowired
    public ControllerUsuario(GestorAdministrador gestorAdministrador, GestorBedel gestorBedel) {
        this.gestorAdministrador = gestorAdministrador;
        this.gestorBedel = gestorBedel;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        switch (usuarioDTO.getTipoUsuario()) {
            case ADMINISTRADOR:
                Integer idAdmin = gestorAdministrador.registrarNuevoAdministrador(usuarioDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body("Administrador creado correctamente con el id: " + idAdmin);
            case BEDEL:
                Integer idBedel = gestorBedel.registrarNuevoBedel(usuarioDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body("Bedel creado correctamente con el id: " + idBedel);
            default:
                throw new BedelException("Tipo de Usuario incorrecto");
        }
    }
}

package com.example.Backend.Controller;

import com.example.Backend.DTO.LoginDTO;
import com.example.Backend.Gestores.GestorUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class InicioSesionController {

    @Autowired
    private GestorUsuarios gestorUsuarios;

    @PostMapping("/iniciarSesion")
    public ResponseEntity<LoginDTO> iniciarSesion(@RequestBody LoginDTO loginDTO) {
        LoginDTO dto = gestorUsuarios.iniciarSesion(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

}


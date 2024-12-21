package com.example.Backend.Controller;

import com.example.Backend.Gestores.Externos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sistema-externo")
@CrossOrigin(origins = "http://localhost:3000")
public class SistemaExternoController {

    @Autowired
    private GestorExterno gestorExterno;

    @GetMapping("/catedras")
    public List<Catedra> obtenerCatedras() {
        return gestorExterno.cargarCatedras();
    }

    @GetMapping("/docentes")
    public List<Docente> obtenerDocentes() {
        return gestorExterno.cargarDocentes();
    }

    @GetMapping("/tipos-reserva")
    public List<TiposReservas> obtenerTiposReservas() {
        return gestorExterno.cargarTiposReservas();
    }

    @GetMapping("/tipos-aula")
    public List<TiposAulas> obtenerTiposAulas() {
        return gestorExterno.cargarTiposAulas();
    }

}
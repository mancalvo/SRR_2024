package com.example.Backend.Gestores.Externos;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestorExterno {

    public List<Catedra> cargarCatedras() {
        return List.of(
                new Catedra(1L, "Diseño de Sistemas"),
                new Catedra(2L, "Base de Datos"),
                new Catedra(3L, "Física II"),
                new Catedra(4L, "Desarrollo de Software")
        );
    }

    public List<Docente> cargarDocentes() {
        return List.of(
                new Docente(1L,"Sanchez Dardo"),
                new Docente(2L, "Benjamin Pozzi"),
                new Docente(3L, "Mariano Frank"),
                new Docente(4L, "Manuel Calvo")
        );
    }

    public List<TiposReservas> cargarTiposReservas() {
        return List.of(
                new TiposReservas(1L,"1er Cuatrimestre"),
                new TiposReservas(2L, "2do Cuatrimestre"),
                new TiposReservas(3L, "Anual"),
                new TiposReservas(4L, "Esporadica")
        );
    }

    public List<TiposAulas> cargarTiposAulas() {
        return List.of(
                new TiposAulas(1L,"Aula Multimedios"),
                new TiposAulas(2L, "Aula Informática"),
                new TiposAulas(3L, "Aula sin recursos")
        );
    }

}

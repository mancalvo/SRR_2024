package com.example.Backend.Gestores.Externos;

import com.example.Backend.Entidades.Periodo;
import com.example.Backend.Enum.Tipo_Periodo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestorPeriodo {

    private List<Periodo> periodos;

    @PostConstruct
    public void init() {
        // Datos simulados de periodos como si provinieran de un sistema externo
        periodos = new ArrayList<>();

        periodos.add(new Periodo(1, Tipo_Periodo.PRIMER_CUATRIMESTRE, 2024, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 6, 30)));
        periodos.add(new Periodo(2, Tipo_Periodo.SEGUNDO_CUATRIMESTRE, 2024, LocalDate.of(2024, 8, 1), LocalDate.of(2024, 11, 30)));
        periodos.add(new Periodo(3, Tipo_Periodo.ANUAL, 2024, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 11, 30)));
        periodos.add(new Periodo(4, Tipo_Periodo.ESPORADICA, 2024, LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 20)));
    }


    public Periodo obtenerPeriodoActual() {
        // LocalDate hoy = LocalDate.now(); // Comentado para probar con una fecha específica
        LocalDate hoy = LocalDate.of(2024, 5, 20); // Fecha fija (20/05/2024)

        return periodos.stream()
                .filter(periodo -> !hoy.isBefore(periodo.getFechaInicio()) && !hoy.isAfter(periodo.getFechaFin()))
                .findFirst()
                .orElse(null);
    }



    public List<Periodo> obtenerTodosLosPeriodos() {
        return periodos;
    }


    public Periodo traerPeriodo(Tipo_Periodo tipoPeriodo) {
        return periodos.stream()
                .filter(periodo -> periodo.getTipo().equals(tipoPeriodo))
                .findFirst()
                .orElse(null);
    }

    public LocalDate calcularFechaInicio(Tipo_Periodo tipoPeriodo) {
        Periodo periodo = traerPeriodo(tipoPeriodo);
        if (periodo != null) {
            return periodo.getFechaInicio();
        }
        throw new IllegalArgumentException("No se encontró un periodo para el tipo especificado.");
    }

    public LocalDate calcularFechaFinal(Tipo_Periodo tipoPeriodo) {
        Periodo periodo = traerPeriodo(tipoPeriodo);
        if (periodo != null) {
            return periodo.getFechaFin();
        }
        throw new IllegalArgumentException("No se encontró un periodo para el tipo especificado.");
    }


}

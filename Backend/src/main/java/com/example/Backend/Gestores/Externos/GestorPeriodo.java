package com.example.Backend.Gestores.Externos;

import com.example.Backend.Enum.Tipo_Periodo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Devuelve el periodo actual en función de la fecha actual.
     *
     * @return Periodo actual o null si no hay ninguno activo.
     */
    public Periodo obtenerPeriodoActual() {
        // LocalDate hoy = LocalDate.now(); // Comentado para probar con una fecha específica
        LocalDate hoy = LocalDate.of(2024, 5, 20); // Fecha fija (20/05/2024)

        return periodos.stream()
                .filter(periodo -> !hoy.isBefore(periodo.getFechaInicio()) && !hoy.isAfter(periodo.getFechaFin()))
                .findFirst()
                .orElse(null);
    }


    /**
     * Devuelve todos los periodos disponibles.
     *
     * @return Lista de periodos.
     */
    public List<Periodo> obtenerTodosLosPeriodos() {
        return periodos;
    }

    /**
     * Filtra y devuelve periodos de tipo general PERIODICA.
     *
     * @return Lista de periodos periódicos.
     */
    public List<Periodo> obtenerPeriodosPeriodicos() {
        return periodos.stream()
                .filter(periodo -> esPeriodoPeriodico(periodo.getTipo()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra y devuelve periodos ESPORADICOS.
     *
     * @return Lista de periodos esporádicos.
     */
    public List<Periodo> obtenerPeriodosEsporadicos() {
        return periodos.stream()
                .filter(periodo -> periodo.getTipo() == Tipo_Periodo.ESPORADICA)
                .collect(Collectors.toList());
    }

    /**
     * Determina si un Tipo_Periodo es PERIODICO.
     */
    private boolean esPeriodoPeriodico(Tipo_Periodo tipo) {
        return tipo == Tipo_Periodo.PRIMER_CUATRIMESTRE
                || tipo == Tipo_Periodo.SEGUNDO_CUATRIMESTRE
                || tipo == Tipo_Periodo.ANUAL;
    }

    /**
     * Obtiene un periodo específico dado un Tipo_Periodo.
     *
     * @param tipoPeriodo Tipo de periodo buscado.
     * @return Periodo correspondiente o null si no existe.
     */
    public Periodo traerPeriodo(Tipo_Periodo tipoPeriodo) {
        return periodos.stream()
                .filter(periodo -> periodo.getTipo().equals(tipoPeriodo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calcula la fecha de inicio para un periodo dado un tipo de periodo.
     *
     * @param tipoPeriodo Tipo_Periodo.
     * @return Fecha de inicio del periodo o null si no se encuentra.
     */
    public LocalDate calcularFechaInicio(Tipo_Periodo tipoPeriodo) {
        Periodo periodo = traerPeriodo(tipoPeriodo);
        if (periodo != null) {
            return periodo.getFechaInicio();
        }
        throw new IllegalArgumentException("No se encontró un periodo para el tipo especificado.");
    }

    /**
     * Calcula la fecha final para un periodo dado un tipo de periodo.
     *
     * @param tipoPeriodo Tipo_Periodo.
     * @return Fecha final del periodo o null si no se encuentra.
     */
    public LocalDate calcularFechaFinal(Tipo_Periodo tipoPeriodo) {
        Periodo periodo = traerPeriodo(tipoPeriodo);
        if (periodo != null) {
            return periodo.getFechaFin();
        }
        throw new IllegalArgumentException("No se encontró un periodo para el tipo especificado.");
    }


}

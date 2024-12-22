package com.example.Backend.Gestores.Externos;

import com.example.Backend.Enum.Tipo_Periodo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
     * Obtiene un periodo específico dado un periodoId.
     *
     * @param periodoId
     * @return Periodo correspondiente o null si no existe.
     */
    public Periodo traerPeriodo(Integer periodoId) {
        return periodos.stream()
                .filter(periodo -> periodo.getId().equals(periodoId))
                .findFirst()
                .orElse(null);
    }
    
        /**
     * Obtiene un periodo específico dado un tipo_periodo y año.
     *
     * @param tipoPeriodo
     * @param anio
     * @return Periodo correspondiente o null si no existe.
     */
    public Periodo traerPeriodo(Tipo_Periodo tipoPeriodo, Integer anio) {
        return periodos.stream()
                .filter(periodo -> periodo.getAnio().equals(anio) && periodo.getTipo() == tipoPeriodo)
                .findFirst()
                .orElse(null);
    }
 
    
    /**
     * Obtiene un periodo específico dado un Tipo_Periodo.
     *
     * @param periodoIds
     * @return Periodos correspondientes o null si no existe.
     */
    public List<Periodo> traerPeriodos(List<Integer> periodoIds) {
        return periodoIds.stream()
                .map(this::traerPeriodo)
                .collect(Collectors.toList());
    }

    /**
     * Calcula la fecha de inicio para un periodo dado un periodoId.
     *
     * @param periodoId
     * @return Fecha de inicio del periodo o null si no se encuentra.
     */
    public LocalDate calcularFechaInicio(Integer periodoId) {
        Periodo periodo = traerPeriodo(periodoId);
        if (periodo != null) {
            return periodo.getFechaInicio();
        }
        throw new IllegalArgumentException("No se encontró un periodo para el tipo especificado.");
    }

    /**
     * Calcula la fecha final para un periodo dado un periodoId.
     *
     * @param periodoId
     * @return Fecha final del periodo o null si no se encuentra.
     */
    public LocalDate calcularFechaFinal(Integer periodoId) {
        Periodo periodo = traerPeriodo(periodoId);
        if (periodo != null) {
            return periodo.getFechaFin();
        }
        throw new IllegalArgumentException("No se encontró un periodo para el tipo especificado.");
    }
 
    public Integer periodoIdQueContieneFecha(LocalDate fecha) {
        for (Periodo periodo : periodos) {
            if ((fecha.isEqual(periodo.getFechaInicio()) || fecha.isAfter(periodo.getFechaInicio())) &&
                (fecha.isEqual(periodo.getFechaFin()) || fecha.isBefore(periodo.getFechaFin()))) {
                return periodo.getId();
            }
        }
        throw new IllegalArgumentException("No se encontró un periodo para la fecha especificada.");
    }
    
    public ArrayList<Integer> obtenerPeriodosMasProximoPorTipo(Tipo_Periodo tipoPeriodo) {
        ArrayList<Integer> ps = new ArrayList<>();
        try {
            if(tipoPeriodo == Tipo_Periodo.PRIMER_CUATRIMESTRE || tipoPeriodo == Tipo_Periodo.SEGUNDO_CUATRIMESTRE) {
                ps.add(periodos.stream()
                    .filter(periodo -> periodo.getTipo() == tipoPeriodo && LocalDate.now().isBefore(periodo.getFechaFin()))
                    .min(Comparator.comparing(Periodo::getFechaInicio))
                    .map(Periodo::getId)
                    .orElse(null));
            } else if (tipoPeriodo == Tipo_Periodo.ANUAL) {
                Integer primerCuatrimestreId = periodos.stream()
                    .filter(periodo -> LocalDate.now().isBefore(periodo.getFechaFin()) 
                            && periodo.getTipo() == Tipo_Periodo.PRIMER_CUATRIMESTRE)
                    .min(Comparator.comparing(Periodo::getFechaInicio))
                    .map(Periodo::getId)
                    .orElse(-1);
            
                Periodo primerCuatrimestre = traerPeriodo(primerCuatrimestreId);
                Integer segundoCuatrimestreId = periodos.stream()
                    .filter(periodo -> primerCuatrimestre.getAnio().compareTo(periodo.getAnio()) == 0 
                            && periodo.getTipo() == Tipo_Periodo.SEGUNDO_CUATRIMESTRE)
                    .min(Comparator.comparing(Periodo::getFechaInicio))
                    .map(Periodo::getId)
                    .orElse(-1);
                ps.add(primerCuatrimestreId);
                ps.add(segundoCuatrimestreId);
            }
        } catch (Exception e) {
            throw e;
        }
            
            
        return ps;
    }
    
}

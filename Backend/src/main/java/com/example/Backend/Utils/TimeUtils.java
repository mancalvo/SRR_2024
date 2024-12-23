package com.example.Backend.Utils;

import com.example.Backend.Entidades.Periodo;
import com.example.Backend.Enum.DiaSemana;
import static com.example.Backend.Enum.DiaSemana.JUEVES;
import static com.example.Backend.Enum.DiaSemana.LUNES;
import static com.example.Backend.Enum.DiaSemana.MARTES;
import static com.example.Backend.Enum.DiaSemana.MIERCOLES;
import static com.example.Backend.Enum.DiaSemana.SABADO;
import static com.example.Backend.Enum.DiaSemana.VIERNES;
import java.time.DayOfWeek;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class TimeUtils {
    
    private TimeUtils() {
        throw new UnsupportedOperationException("Clase Utils no puede ser instanciada");
    }

    
    public static DayOfWeek convertirDiaSemanaADayOfWeek(DiaSemana diaSemana) {
        switch (diaSemana) {
            case LUNES: return DayOfWeek.MONDAY;
            case MARTES: return DayOfWeek.TUESDAY;
            case MIERCOLES: return DayOfWeek.WEDNESDAY;
            case JUEVES: return DayOfWeek.THURSDAY;
            case VIERNES: return DayOfWeek.FRIDAY;
            case SABADO: return DayOfWeek.SATURDAY;
            default: throw new IllegalArgumentException("Día de la semana no válido: " + diaSemana);
        }
    }
    
    public static DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return DiaSemana.LUNES;
            case TUESDAY: return DiaSemana.MARTES;
            case WEDNESDAY: return DiaSemana.MIERCOLES;
            case THURSDAY: return DiaSemana.JUEVES;
            case FRIDAY: return DiaSemana.VIERNES;
            case SATURDAY: return DiaSemana.SABADO;
            default: throw new IllegalArgumentException("Día de la semana no válido: " + dayOfWeek);
        }
    }
       
    public static boolean hayConflictoHorario(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && inicio2.isBefore(fin1);
    }
    
    public static ArrayList<LocalDate> obtenerFechasParaPeriodosYDia(DayOfWeek dia, List<Periodo> periodos) {
        ArrayList<LocalDate> fechas = new ArrayList<>();

        for (Periodo p : periodos) {
            LocalDate actual = p.getFechaInicio();
            LocalDate fechaFinal = p.getFechaFin();

            while (actual.getDayOfWeek() != dia) {
                actual = actual.plusDays(1);
            }

            while (!actual.isAfter(fechaFinal)) {
                fechas.add(actual);
                actual = actual.plusWeeks(1);
            }
        }

        return fechas;
    }
    
        public static ArrayList<LocalDate> obtenerFechasParaPeriodosYDia(DayOfWeek dia, Periodo periodo) {
        return obtenerFechasParaPeriodosYDia(dia, List.of(periodo));
    }
    
}

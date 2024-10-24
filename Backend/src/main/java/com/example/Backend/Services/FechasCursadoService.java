package com.example.Backend.Services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FechasCursadoService {

    public FechasCursado cargarFechas() {

        FechasCursado fechas = new FechasCursado();
        fechas.setInicioAnual(LocalDate.of(2024, 3, 1));
        fechas.setFinAnual(LocalDate.of(2024, 11, 30));
        fechas.setInicioCuatrimestre1(LocalDate.of(2024, 3, 1));
        fechas.setFinCuatrimestre1(LocalDate.of(2024, 6, 30));
        fechas.setInicioCuatrimestre2(LocalDate.of(2024, 7, 1));
        fechas.setFinCuatrimestre2(LocalDate.of(2024, 11, 30));
        return fechas;
    }
}

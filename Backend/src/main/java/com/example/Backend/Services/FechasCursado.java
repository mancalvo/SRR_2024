package com.example.Backend.Services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechasCursado {
    private LocalDate inicioAnual;
    private LocalDate finAnual;
    private LocalDate inicioCuatrimestre1;
    private LocalDate finCuatrimestre1;
    private LocalDate inicioCuatrimestre2;
    private LocalDate finCuatrimestre2;
}

package com.example.Backend.Entidades;

import com.example.Backend.Enum.Tipo_Periodo;
import lombok.*;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Periodo {

    private Integer id;
    private Tipo_Periodo tipo_periodo;
    private Integer anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tipo_Periodo getTipo_periodo() {
        return tipo_periodo;
    }

    public void setTipo_periodo(Tipo_Periodo tipo_periodo) {
        this.tipo_periodo = tipo_periodo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}

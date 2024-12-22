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
    private Tipo_Periodo tipoPeriodo;
    private Integer anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tipo_Periodo getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(Tipo_Periodo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
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

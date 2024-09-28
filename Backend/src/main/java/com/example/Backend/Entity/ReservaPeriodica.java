package com.example.Backend.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Periodica")
public class ReservaPeriodica extends Reserva {

    private String periodo; // Ej: "1 Cuatrimestre", "2 Cuatrimestres", "Anual"

    @ElementCollection
    private List<DiaReserva> diasReserva; // Relación para los días de la semana

}

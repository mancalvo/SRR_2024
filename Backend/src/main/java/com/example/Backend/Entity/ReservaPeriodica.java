package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservaPeriodica extends Reserva {

    private String periodo; // Ej: "1 Cuatrimestre", "2 Cuatrimestres", "Anual"

    @OneToMany(mappedBy = "reservaPeriodica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaPeriodicaDiasReserva> diasReserva = new ArrayList<>();

    public void addDiaReserva(ReservaPeriodicaDiasReserva diaReserva) {
        diasReserva.add(diaReserva);
        diaReserva.setReservaPeriodica(this);
    }

    public void removeDiaReserva(ReservaPeriodicaDiasReserva diaReserva) {
        diasReserva.remove(diaReserva);
        diaReserva.setReservaPeriodica(null);
    }

}
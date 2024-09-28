package com.example.Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aulas_multimedios")
public class AulaMultimedios extends Aula{

    private boolean televisor;
    private boolean canion;
    private boolean computadora;
    private String pizarron;
    private boolean aireAcondicionado;
    private boolean ventiladores;

}

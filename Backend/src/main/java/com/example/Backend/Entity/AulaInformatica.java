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
@Table(name = "aulas_informatica")
public class AulaInformatica extends Aula{

    private int cantidadPCs;
    private String pizarron;
    private boolean canion;
    private boolean aireAcondicionado;


}

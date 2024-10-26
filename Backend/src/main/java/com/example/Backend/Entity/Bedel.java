package com.example.Backend.Entity;

import com.example.Backend.Enum.Tipo_Turno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bedeles")
public class Bedel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String apellido;
    private String nombre;
    private String contrasena;
    @Enumerated(EnumType.STRING)
    private Tipo_Turno turno;
    private Boolean activo;
}

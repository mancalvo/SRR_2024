package com.example.Backend.Entidades;

import com.example.Backend.Enum.Tipo_Turno;
import com.example.Backend.Enum.Tipo_Usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "contraseña")
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    private Tipo_Usuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_turno")
    private Tipo_Turno tipoTurno;

    @Column(name = "activo")
    private Boolean activo;
}


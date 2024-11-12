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

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "contraseña", nullable = false)
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private Tipo_Usuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_turno")
    private Tipo_Turno tipoTurno;

    @Column(name = "activo")
    private Boolean activo;
}


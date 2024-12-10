package com.example.Backend.DAO;

import com.example.Backend.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorDAO extends JpaRepository<Usuario,Integer> {
    boolean existsByNombreAndApellido(String nombre, String apellido);

    Optional<Usuario> findByNombreUsuarioAndContrasenia(String nombreUsuario, String contrasenia);
    boolean existsByNombreUsuario(String nombreUsuario);
}

package com.example.Backend.DAO;

import com.example.Backend.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelDAO extends JpaRepository <Usuario,Integer> {

    boolean existsByNombreAndApellido(String nombre, String apellido);
}

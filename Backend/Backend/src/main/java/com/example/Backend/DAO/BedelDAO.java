package com.example.Backend.DAO;

import com.example.Backend.Entidades.Usuario;
import com.example.Backend.Enum.Tipo_Turno;
import com.example.Backend.Enum.Tipo_Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedelDAO extends JpaRepository <Usuario,Integer> {

    boolean existsByNombreAndApellido(String nombre, String apellido);

    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id AND u.tipoUsuario = 'BEDEL' AND u.activo = true")
    Optional<Usuario> buscarBedelPorId(@Param("id") Integer id);

    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = 'BEDEL' AND u.activo = true")
    List<Usuario> buscarTodosLosBedels();

    List<Usuario> findByApellido(String apellido);
    List<Usuario> findByTipoTurno(Tipo_Turno tipoTurno);
    List<Usuario> findByApellidoAndTipoTurno(String apellido, Tipo_Turno tipoTurno);

    Optional<Usuario> findByNombreUsuarioAndTipoUsuarioAndActivo(String nombreUsuario, Tipo_Usuario tipoUsuario, boolean activo);

}

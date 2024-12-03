package com.example.Backend.Gestores;

import com.example.Backend.DAO.BedelDAO;
import com.example.Backend.DTO.BedelDTO;
import com.example.Backend.DTO.UsuarioDTO;
import com.example.Backend.Entidades.Usuario;
import com.example.Backend.Enum.Tipo_Turno;
import com.example.Backend.Enum.Tipo_Usuario;
import com.example.Backend.Exceptions.BedelException;
import com.example.Backend.Gestores.Externos.GestorContrasenia;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GestorBedel {

    @Autowired
    private final BedelDAO bedelDAO;
    @Autowired
    private final GestorContrasenia gestorContrasenia;

    public Integer registrarNuevoBedel(UsuarioDTO usuarioDTO) {

        if (!validarPoliticasContrasenia(usuarioDTO.getContrasenia())) {
            throw new BedelException("Políticas de contraseña inválidas");
        }

        if (encontrarBedelPorNyA(usuarioDTO.getNombre(), usuarioDTO.getApellido())) {
            throw new BedelException("Ya existe un usuario registrado con ese nombre y apellido");
        }

        validarUsuario(usuarioDTO);

        Usuario bedel = new Usuario();
        bedel.setNombre(usuarioDTO.getNombre());
        bedel.setApellido(usuarioDTO.getApellido());
        bedel.setNombreUsuario(usuarioDTO.getNombreUsuario());
        bedel.setContrasenia(usuarioDTO.getContrasenia());
        bedel.setTipoUsuario(usuarioDTO.getTipoUsuario());
        bedel.setTipoTurno(usuarioDTO.getTipoTurno());
        bedel.setActivo(true);

        bedel = bedelDAO.save(bedel);
        return bedel.getIdUsuario();
    }

    public Boolean validarPoliticasContrasenia (String contrasenia) {
        return gestorContrasenia.validarPoliticasContrasenia(contrasenia);
    }

    public Boolean encontrarBedelPorNyA(String nombre, String apellido) {
        return bedelDAO.existsByNombreAndApellido(nombre,apellido);
    }

    public void validarUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().isEmpty()) {
            throw new BedelException("El nombre no puede estar vacío");
        }

        if (usuarioDTO.getApellido() == null || usuarioDTO.getApellido().isEmpty()) {
            throw new BedelException("El apellido no puede estar vacío");
        }

        if (usuarioDTO.getNombreUsuario() == null || usuarioDTO.getNombreUsuario().isEmpty()) {
            throw new BedelException("El nombre de usuario no puede estar vacío");
        }

        if (!usuarioDTO.getContrasenia().equals(usuarioDTO.getRepetirContrasenia())) {
            throw new BedelException("Las contraseñas deben ser iguales");
        }

        if (usuarioDTO.getTipoUsuario() == null) {
            throw new BedelException("El tipo de usuario no puede ser nulo");
        } else if (!usuarioDTO.getTipoUsuario().equals(Tipo_Usuario.BEDEL)) {
            throw new BedelException("El tipo de usuario debe ser BEDEL");
        }

        if (usuarioDTO.getTipoTurno() == null) {
            throw new BedelException("El tipo de turno no puede ser nulo");
        }

    }


    public UsuarioDTO buscarBedelPorId(Integer id) {
        Optional<Usuario> usuarioBBDD = bedelDAO.buscarBedelPorId(id);

        if (usuarioBBDD.isEmpty()) {
            throw new BedelException("No se encontró un Bedel con el ID: " + id);
        }

        UsuarioDTO usuariodto = new UsuarioDTO();

        // Convertimos el Usuario a BedelDTO


        usuariodto.setIdUsuario(usuarioBBDD.get().getIdUsuario());
        usuariodto.setNombre(usuarioBBDD.get().getNombre());
        usuariodto.setApellido(usuarioBBDD.get().getApellido());
        usuariodto.setNombreUsuario(usuarioBBDD.get().getNombreUsuario());
        usuariodto.setContrasenia(usuarioBBDD.get().getContrasenia());
        usuariodto.setTipoUsuario(usuarioBBDD.get().getTipoUsuario());
        usuariodto.setTipoTurno(usuarioBBDD.get().getTipoTurno());
        usuariodto.setActivo(usuarioBBDD.get().getActivo());

        return usuariodto;


    }

    public List<BedelDTO> obtenerTodosLosBedels() {
        List<Usuario> bedels = bedelDAO.buscarTodosLosBedels();
        return bedels.stream().map(usuario -> {
            BedelDTO dto = new BedelDTO();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setNombreUsuario(usuario.getNombreUsuario());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setTipoTurno(usuario.getTipoTurno());
            return dto;
        }).collect(Collectors.toList());
    }


    public void actualizarBedel(Integer id, UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOpt = bedelDAO.buscarBedelPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new BedelException("No se encontro el usuario que se quiere modificar"); // No se encontró el usuario
        }

        Usuario usuario = usuarioOpt.get();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTipoTurno(usuarioDTO.getTipoTurno());
        usuario.setContrasenia(usuarioDTO.getContrasenia());

        bedelDAO.save(usuario);
    }

    public void eliminarBedel(Integer id) {
        Optional<Usuario> usuarioOpt = bedelDAO.buscarBedelPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new BedelException("No se encontro el bedel a eliminar");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(false);  // Marcamos el usuario como eliminado

        bedelDAO.save(usuario);
    }

}


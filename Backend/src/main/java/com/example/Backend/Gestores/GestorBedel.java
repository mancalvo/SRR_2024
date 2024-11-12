package com.example.Backend.Gestores;

import com.example.Backend.DAO.BedelDAO;
import com.example.Backend.DTO.UsuarioDTO;
import com.example.Backend.Entidades.Usuario;
import com.example.Backend.Enum.Tipo_Usuario;
import com.example.Backend.Exceptions.BedelException;
import com.example.Backend.Gestores.Externos.GestorContrasenia;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GestorBedel {


    private final BedelDAO bedelDAO;

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


}


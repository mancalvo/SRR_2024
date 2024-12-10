package com.example.Backend.Gestores;

import com.example.Backend.DAO.AdministradorDAO;
import com.example.Backend.DTO.UsuarioDTO;
import com.example.Backend.Entidades.Usuario;
import com.example.Backend.Enum.Tipo_Usuario;
import com.example.Backend.Exceptions.BedelException;
import com.example.Backend.Gestores.Externos.GestorContrasenia;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GestorAdministrador {

    @Autowired
    private final GestorContrasenia gestorContrasenia;
    @Autowired
    private final GestorBedel gestorBedel;
    @Autowired
    private final AdministradorDAO administradorDAO;

    public Integer registrarNuevoAdministrador(UsuarioDTO usuarioDTO) {
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

        bedel = administradorDAO.save(bedel);
        return bedel.getIdUsuario();
    }

    public Boolean validarPoliticasContrasenia (String contrasenia) {
        return gestorContrasenia.validarPoliticasContrasenia(contrasenia);
    }

    public Boolean encontrarBedelPorNyA(String nombre, String apellido) {
        return administradorDAO.existsByNombreAndApellido(nombre,apellido);
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
        } else if (!usuarioDTO.getTipoUsuario().equals(Tipo_Usuario.ADMINISTRADOR)) {
            throw new BedelException("El tipo de usuario debe ser ADMINISTRADOR");
        }



    }


    public Integer registrarNuevoUsuario(UsuarioDTO usuarioDTO) {
        if (administradorDAO.existsByNombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new BedelException("El usuario ya se encuentra registrado");
        }
        Integer id = 0;
        switch (usuarioDTO.getTipoUsuario()) {
            case ADMINISTRADOR:
                id = registrarNuevoAdministrador(usuarioDTO);
                break;
            case BEDEL:
                id = gestorBedel.registrarNuevoBedel(usuarioDTO);
                break;
        }
        return id;
    }

}

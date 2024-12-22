package com.example.Backend.Gestores;


import com.example.Backend.DAO.AdministradorDAO;
import com.example.Backend.DTO.LoginDTO;
import com.example.Backend.Entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GestorUsuarios {

    @Autowired
    private AdministradorDAO administradorDAO;

    public LoginDTO iniciarSesion(LoginDTO loginDTO) {
        LoginDTO response = new LoginDTO();

        Optional<Usuario> usuario = Optional.empty();

        usuario = administradorDAO.findByNombreUsuarioAndContrasenia(loginDTO.getNombreUsuario(), loginDTO.getContrasenia());

        if (usuario.isEmpty()) {
            response.setMensaje("Credenciales inválidas");
            response.setHabilitado(false);
            return response;
        }

        response.setNombreUsuario(usuario.get().getNombreUsuario());
        response.setContrasenia(usuario.get().getContrasenia());
        response.setTipoUsuario(usuario.get().getTipoUsuario());
        response.setHabilitado(true);
        response.setMensaje("Inicio de sesión exitoso");

        return response;
    }
}
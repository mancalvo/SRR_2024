package com.example.Backend.Gestores.Externos;

import org.springframework.stereotype.Service;

@Service
public class GestorContrasenia {

    public Boolean validarPoliticasContrasenia (String contrasenia) {
        if (contrasenia.length() < 8) {
            return false;
        }
        return true;
    }

}

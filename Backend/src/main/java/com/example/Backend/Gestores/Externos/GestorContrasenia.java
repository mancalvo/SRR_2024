package com.example.Backend.Gestores.Externos;

import org.springframework.stereotype.Service;

@Service
public class GestorContrasenia {

    private static final int LONGITUD_MINIMA = 8;
    private static final boolean REQUIERE_SIGNOS_ESPECIALES = true;
    private static final boolean REQUIERE_MAYUSCULA = true;
    private static final boolean REQUIERE_DIGITO = true;

    public Boolean validarPoliticasContrasenia(String contrasenia) {

        if (contrasenia.length() < LONGITUD_MINIMA) {
            return false;
        }


        if (REQUIERE_SIGNOS_ESPECIALES && !contrasenia.matches(".*[@#$%&*].*")) {
            return false;
        }


        if (REQUIERE_MAYUSCULA && !contrasenia.matches(".*[A-Z].*")) {
            return false;
        }


        if (REQUIERE_DIGITO && !contrasenia.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }
}

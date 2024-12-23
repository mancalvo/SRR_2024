package com.example.Backend.Gestores.StrategyValidarDisponibilidadAula;

import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.Entidades.Aula;
import java.util.HashMap;
import java.util.List;
import com.example.Backend.DAO.ReservaPeriodicaDAO;
import com.example.Backend.DAO.ReservaEsporadicaDAO;

public interface IValidarDisponibilidad {

    AulaDisponibilidadResponseDTO validarDisponibilidad(
            AulaDisponibilidadRequestDTO requestDTO,
            List<Aula> aulasCompatibles
    );

}

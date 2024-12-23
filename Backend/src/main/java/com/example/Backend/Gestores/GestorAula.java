package com.example.Backend.Gestores;

import com.example.Backend.DAO.*;
import com.example.Backend.DTO.AulaDisponibilidadRequestDTO;
import com.example.Backend.DTO.AulaDisponibilidadResponseDTO;
import com.example.Backend.Entidades.*;
import com.example.Backend.Gestores.StrategyValidarDisponibilidadAula.DisponibilidadStrategyFactory;
import com.example.Backend.Gestores.StrategyValidarDisponibilidadAula.IValidarDisponibilidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GestorAula {

    @Autowired
    private AulaInformaticaDAO aulaInformaticaDAO;
    @Autowired
    private AulaMultimediosDAO aulaMultimediosDAO;
    @Autowired
    private AulaSinRecursosDAO aulaSinRecursosDAO;
    @Autowired
    private DisponibilidadStrategyFactory disponibilidadStrategyFactory; // Inyectamos la fábrica

    public AulaDisponibilidadResponseDTO buscarAulasDisponibles(AulaDisponibilidadRequestDTO requestDTO) {

        // Lista para almacenar las aulas disponibles
        List<Aula> aulasDisponibles = new ArrayList<>();

        // Buscar aulas según el tipo
        if ("INFORMATICA".equalsIgnoreCase(requestDTO.getTipoAula())) {
            // Buscar Aulas de Informática
            List<AulaInformatica> aulasInformatica = aulaInformaticaDAO.findByCapacidad(requestDTO.getCapacidad());
            aulasDisponibles.addAll(aulasInformatica);
        } else if ("MULTIMEDIOS".equalsIgnoreCase(requestDTO.getTipoAula())) {
            // Buscar Aulas Multimedios
            List<AulaMultimedios> aulasMultimedios = aulaMultimediosDAO.findByCapacidad(requestDTO.getCapacidad());
            aulasDisponibles.addAll(aulasMultimedios);
        } else if ("SINRECURSOS".equalsIgnoreCase(requestDTO.getTipoAula())) {
            // Buscar Aulas sin recursos
            List<AulaSinRecursos> aulasSinRecursos = aulaSinRecursosDAO.findByCapacidad(requestDTO.getCapacidad());
            aulasDisponibles.addAll(aulasSinRecursos);
        } else {
            throw new IllegalArgumentException("El tipo de aula debe ser 'INFORMATICA', 'MULTIMEDIOS' o 'SINRECURSOS'.");
        }

        IValidarDisponibilidad estrategia = disponibilidadStrategyFactory.getStrategy(requestDTO.getTipoReserva());

        return estrategia.validarDisponibilidad(requestDTO, aulasDisponibles);

    }

}

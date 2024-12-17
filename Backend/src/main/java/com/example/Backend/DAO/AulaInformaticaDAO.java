package com.example.Backend.DAO;

import com.example.Backend.Entidades.AulaInformatica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaInformaticaDAO extends JpaRepository<AulaInformatica, Integer> {

}


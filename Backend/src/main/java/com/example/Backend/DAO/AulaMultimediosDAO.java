package com.example.Backend.DAO;

import com.example.Backend.Entidades.AulaMultimedios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaMultimediosDAO extends JpaRepository<AulaMultimedios, Integer> {

}


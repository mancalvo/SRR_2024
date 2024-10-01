package com.example.Backend.Repository;

import com.example.Backend.Entity.AulaInformatica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaInformaticaRepository extends JpaRepository<AulaInformatica,Long> {
}

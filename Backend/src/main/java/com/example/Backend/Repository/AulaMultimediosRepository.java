package com.example.Backend.Repository;

import com.example.Backend.Entity.AulaMultimedios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaMultimediosRepository extends JpaRepository<AulaMultimedios,Long> {
}

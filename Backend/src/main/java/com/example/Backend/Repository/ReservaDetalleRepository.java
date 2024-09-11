package com.example.Backend.Repository;

import com.example.Backend.Entity.ReservaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaDetalleRepository extends JpaRepository<ReservaDetalle,Long> {
}

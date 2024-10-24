package com.example.Backend.Repository;

import com.example.Backend.Entity.Bedel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedelRepository extends JpaRepository<Bedel,Long> {

    @Query("SELECT b FROM Bedel b WHERE b.activo = true")
    List<Bedel> findAllActive();

    @Query("SELECT b FROM Bedel b WHERE b.id = :id AND b.activo = true")
    Optional<Bedel> findByIdAndActive(@Param("id") Long id);


    boolean existsByNombreAndApellido(String nombre, String apellido);

    @Query("SELECT b FROM Bedel b WHERE b.nombre = :nombre AND b.apellido = :apellido")
    Optional<Bedel> findByNombreAndApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);
}

package com.example.Backend.Repository;

import com.example.Backend.Entity.Bedel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelRepository extends JpaRepository<Bedel,Long> {
}

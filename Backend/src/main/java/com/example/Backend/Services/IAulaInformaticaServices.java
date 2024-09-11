package com.example.Backend.Services;

import com.example.Backend.Entity.AulaInformatica;

import java.util.List;
import java.util.Optional;

public interface IAulaInformaticaServices {

    public void create (AulaInformatica aulaInformatica) ;
    public void update (AulaInformatica aulaInformatica) ;
    public void deleteById (Long id) ;
    public Optional<AulaInformatica> findById (Long id) ;
    public List<AulaInformatica> findAll ();
}

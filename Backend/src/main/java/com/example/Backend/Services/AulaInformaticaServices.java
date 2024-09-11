package com.example.Backend.Services;

import com.example.Backend.Entity.AulaInformatica;
import com.example.Backend.Repository.AulaInformaticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaInformaticaServices implements IAulaInformaticaServices{

    @Autowired
    private AulaInformaticaRepository repoAulaInformatica;

    @Override
    public void create(AulaInformatica aulaInformatica) {
        repoAulaInformatica.save(aulaInformatica);
    }

    @Override
    public void update(AulaInformatica aulaInformatica) {
        repoAulaInformatica.save(aulaInformatica);
    }

    @Override
    public void deleteById(Long id) {
        repoAulaInformatica.deleteById(id);
    }

    @Override
    public Optional<AulaInformatica> findById(Long id) {
        return repoAulaInformatica.findById(id);
    }

    @Override
    public List<AulaInformatica> findAll() {
        return repoAulaInformatica.findAll();
    }
}

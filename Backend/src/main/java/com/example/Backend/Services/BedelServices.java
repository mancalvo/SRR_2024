package com.example.Backend.Services;

import com.example.Backend.Entity.Bedel;
import com.example.Backend.Repository.BedelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BedelServices implements IBedelServices {

    @Autowired
    private BedelRepository repoBedel;

    @Override
    public void create(Bedel bedel) {
        repoBedel.save(bedel);
    }

    @Override
    public void update(Bedel bedel) {
        repoBedel.save(bedel);
    }

    @Override
    public void deleteById(Long id) {
        repoBedel.deleteById(id);
    }

    @Override
    public Optional<Bedel> findById(Long id) {
        return repoBedel.findById(id);
    }

    @Override
    public List<Bedel> findAll() {
        return repoBedel.findAll();
    }
}
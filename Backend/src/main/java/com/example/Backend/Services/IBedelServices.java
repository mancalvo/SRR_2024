package com.example.Backend.Services;

import com.example.Backend.DTO.BedelDTO;
import com.example.Backend.Entity.Bedel;

import java.util.List;
import java.util.Optional;

public interface IBedelServices {
    BedelDTO create(BedelDTO bedel);
    BedelDTO update(Long id, BedelDTO bedel);
    void deleteById(Long id);
    BedelDTO findById(Long id);
    List<BedelDTO> findAll();
    BedelDTO activarBedel(BedelDTO bedelDto);
}


package com.example.Backend.Services;

import com.example.Backend.DTO.BedelDTORequest;
import com.example.Backend.DTO.BedelDTOResponse;

import java.util.List;

public interface IBedelServices {
    BedelDTOResponse create(BedelDTORequest bedel);
    BedelDTOResponse update(Long id, BedelDTORequest bedel);
    void deleteById(Long id);
    BedelDTOResponse findById(Long id);
    List<BedelDTOResponse> findAll();
    BedelDTOResponse activarBedel(BedelDTORequest bedelDtoRequest);
}


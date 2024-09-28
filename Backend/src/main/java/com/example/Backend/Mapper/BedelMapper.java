package com.example.Backend.Mapper;

import com.example.Backend.DTO.BedelDTO;
import com.example.Backend.Entity.Bedel;
import org.springframework.stereotype.Component;

@Component
public abstract class BedelMapper {

    public static BedelDTO BedelToDTO(Bedel bedel) {
        BedelDTO bedelDTO = new BedelDTO();
        bedelDTO.setId(bedel.getId());
        bedelDTO.setNombre(bedel.getNombre());
        bedelDTO.setApellido(bedel.getApellido());
        bedelDTO.setTurno(bedel.getTurno());
        return bedelDTO;
    }

    public static Bedel DtoToBedel(BedelDTO dto) {
        Bedel bedel = new Bedel();
        bedel.setId(dto.getId());
        bedel.setNombre(dto.getNombre());
        bedel.setApellido(dto.getApellido());
        bedel.setTurno(dto.getTurno());
        return bedel;
    }

}

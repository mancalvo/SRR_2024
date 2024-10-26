package com.example.Backend.Mapper;

import com.example.Backend.DTO.BedelDTORequest;
import com.example.Backend.DTO.BedelDTOResponse;
import com.example.Backend.Entity.Bedel;
import org.springframework.stereotype.Component;

@Component
public abstract class BedelMapper {

    public static BedelDTORequest BedelToDTORequest(Bedel bedel) {
        BedelDTORequest bedelDTORequest = new BedelDTORequest();
        bedelDTORequest.setId(bedel.getId());
        bedelDTORequest.setNombre(bedel.getNombre());
        bedelDTORequest.setApellido(bedel.getApellido());
        bedelDTORequest.setTurno(bedel.getTurno());
        bedelDTORequest.setActivo(bedel.getActivo());
        return bedelDTORequest;
    }

    public static BedelDTOResponse BedelToDTOResponse (Bedel bedel) {
        BedelDTOResponse bedelDTOResponse = new BedelDTOResponse();
        bedelDTOResponse.setId(bedel.getId());
        bedelDTOResponse.setNombre(bedel.getNombre());
        bedelDTOResponse.setApellido(bedel.getApellido());
        bedelDTOResponse.setTurno(bedel.getTurno());
        return bedelDTOResponse;
    }

    public static Bedel DtoRequestToBedel(BedelDTORequest dto) {
        Bedel bedel = new Bedel();
        bedel.setId(dto.getId());
        bedel.setNombre(dto.getNombre());
        bedel.setApellido(dto.getApellido());
        bedel.setTurno(dto.getTurno());
        bedel.setActivo(dto.getActivo());
        bedel.setContrasena(dto.getContrasena());
        return bedel;
    }

}

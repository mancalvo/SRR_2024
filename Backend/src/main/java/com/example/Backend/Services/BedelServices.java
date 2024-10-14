package com.example.Backend.Services;

import com.example.Backend.DTO.BedelDTO;
import com.example.Backend.Entity.Bedel;
import com.example.Backend.Enum.Tipo_Turno;
import com.example.Backend.Exceptions.BedelNotFoundException;
import com.example.Backend.Exceptions.InvalidBedelDataException;
import com.example.Backend.Mapper.BedelMapper;
import com.example.Backend.Repository.BedelRepository;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BedelServices implements IBedelServices {

    private final BedelRepository repoBedel;

    public BedelServices(BedelRepository repoBedel) {
        this.repoBedel = repoBedel;
    }

    @Override
    public BedelDTO create(BedelDTO bedelDto) {
        validateBedelData(bedelDto);

        Bedel bedel = BedelMapper.DtoToBedel(bedelDto);
        Bedel savedBedel = repoBedel.save(bedel);
        return BedelMapper.BedelToDTO(savedBedel);
    }

    @Override
    public BedelDTO update(Long id, BedelDTO bedelDto) {
        validateBedelData(bedelDto);
        Bedel bedel = repoBedel.findById(id)
                .orElseThrow(() -> new BedelNotFoundException(id));

        Optional.ofNullable(bedelDto.getNombre()).ifPresent(bedel::setNombre);
        Optional.ofNullable(bedelDto.getApellido()).ifPresent(bedel::setApellido);
        Optional.ofNullable(bedelDto.getTurno()).ifPresent(bedel::setTurno);

        Bedel savedBedel = repoBedel.save(bedel);
        return BedelMapper.BedelToDTO(savedBedel);
    }

    @Override
    public void deleteById(Long id) {
        Bedel bedel = repoBedel.findById(id)
                .orElseThrow(() -> new BedelNotFoundException(id));
        repoBedel.delete(bedel);
    }

    @Override
    public BedelDTO findById(Long id) {
        Bedel bedel = repoBedel.findById(id)
                .orElseThrow(() -> new BedelNotFoundException(id));
        return BedelMapper.BedelToDTO(bedel);
    }

    @Override
    public List<BedelDTO> findAll() {
        return repoBedel.findAll().stream()
                .map(BedelMapper::BedelToDTO)
                .collect(Collectors.toList());
    }

    private void validateBedelData(BedelDTO bedelDto) {
        if (bedelDto.getNombre() == null || bedelDto.getNombre().isEmpty()) {
            throw new InvalidBedelDataException("El nombre del Bedel no puede estar vacío.");
        }
        if (bedelDto.getApellido() == null || bedelDto.getApellido().isEmpty()) {
            throw new InvalidBedelDataException("El apellido del Bedel no puede estar vacío.");
        }

        // Validar que el turno no sea nulo
        if (bedelDto.getTurno() == null) {
            throw new InvalidBedelDataException("El tipo de turno no puede ser nulo.");
        }

        // Validar que el turno sea un valor válido del enum
        try {
            Tipo_Turno.valueOf(bedelDto.getTurno().name());
        } catch (IllegalArgumentException e) {
            throw new InvalidBedelDataException("El tipo de turno no es válido.");
        }

    }



}

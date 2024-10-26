package com.example.Backend.Services;

import com.example.Backend.DTO.BedelDTORequest;
import com.example.Backend.DTO.BedelDTOResponse;
import com.example.Backend.Entity.Bedel;
import com.example.Backend.Enum.Tipo_Turno;
import com.example.Backend.Exceptions.BedelNotFoundException;
import com.example.Backend.Exceptions.InvalidBedelDataException;
import com.example.Backend.Mapper.BedelMapper;
import com.example.Backend.Repository.BedelRepository;
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
    public BedelDTOResponse create(BedelDTORequest bedelDtoRequest) {

        validateBedelData(bedelDtoRequest);

        if(repoBedel.existsByNombreAndApellido(bedelDtoRequest.getNombre(), bedelDtoRequest.getApellido())){
            throw new InvalidBedelDataException("Ya existe el bedel: " + bedelDtoRequest.getNombre()+ " " + bedelDtoRequest.getApellido());
        }

        Bedel bedel = BedelMapper.DtoRequestToBedel(bedelDtoRequest);
        bedel.setActivo(true);
        Bedel savedBedel = repoBedel.save(bedel);
        return BedelMapper.BedelToDTOResponse(savedBedel);
    }

    @Override
    public BedelDTOResponse update(Long id, BedelDTORequest bedelDtoRequest) {
        validateBedelData(bedelDtoRequest);
        Bedel bedel = repoBedel.findById(id)
                .orElseThrow(() -> new BedelNotFoundException(id));

        Optional.ofNullable(bedelDtoRequest.getNombre()).ifPresent(bedel::setNombre);
        Optional.ofNullable(bedelDtoRequest.getApellido()).ifPresent(bedel::setApellido);
        Optional.ofNullable(bedelDtoRequest.getTurno()).ifPresent(bedel::setTurno);
        Optional.ofNullable(bedelDtoRequest.getActivo()).ifPresent(bedel::setActivo);
        Optional.ofNullable(bedelDtoRequest.getContrasena()).ifPresent(bedel::setContrasena);

        Bedel savedBedel = repoBedel.save(bedel);
        return BedelMapper.BedelToDTOResponse(savedBedel);
    }

    @Override
    public void deleteById(Long id) {
        Bedel bedel = repoBedel.findById(id)
                .orElseThrow(() -> new BedelNotFoundException(id));
        bedel.setActivo(false);
        repoBedel.save(bedel);
    }

    @Override
    public BedelDTOResponse findById(Long id) {
        Bedel bedel = repoBedel.findByIdAndActive(id)
                .orElseThrow(() -> new BedelNotFoundException(id));
        return BedelMapper.BedelToDTOResponse(bedel);
    }


    @Override
    public List<BedelDTOResponse> findAll() {
        return repoBedel.findAllActive().stream()
                .map(BedelMapper::BedelToDTOResponse)
                .collect(Collectors.toList());
    }


    @Override
    public BedelDTOResponse activarBedel(BedelDTORequest bedelDtoRequest) {
        if(bedelDtoRequest.getNombre().isEmpty() ||  bedelDtoRequest.getApellido().isEmpty()){
            throw new InvalidBedelDataException("Se debe ingresar el nombre y el apellido");
        }

        Bedel bedel = repoBedel.findByNombreAndApellido(bedelDtoRequest.getNombre(), bedelDtoRequest.getApellido())
                .orElseThrow(() -> new InvalidBedelDataException("El bedel con nombre " + bedelDtoRequest.getNombre() + " y apellido " + bedelDtoRequest.getApellido() + " no fue encontrado"));

        bedel.setActivo(true);
        Bedel bedelActualizado = repoBedel.save(bedel);

        return BedelMapper.BedelToDTOResponse(bedelActualizado);
    }


    private void validateBedelData(BedelDTORequest bedelDtoRequest) {
        if (bedelDtoRequest.getNombre() == null || bedelDtoRequest.getNombre().isEmpty()) {
            throw new InvalidBedelDataException("El nombre del Bedel no puede estar vacío.");
        }
        if (bedelDtoRequest.getApellido() == null || bedelDtoRequest.getApellido().isEmpty()) {
            throw new InvalidBedelDataException("El apellido del Bedel no puede estar vacío.");
        }
        // Validar que las contraseñas sean iguales
        if(!bedelDtoRequest.getContrasena().equals(bedelDtoRequest.getRepetirContrasena())){
            throw new InvalidBedelDataException("Las contraseñas deben ser iguales.");
        }

        if(bedelDtoRequest.getContrasena().isEmpty() || bedelDtoRequest.getRepetirContrasena().isEmpty()){
            throw new InvalidBedelDataException("Se debe ingresar una contraseña");
        }

        validarPassword(bedelDtoRequest.getContrasena());

        // Validar que el turno no sea nulo
        if (bedelDtoRequest.getTurno() == null) {
            throw new InvalidBedelDataException("El tipo de turno no puede ser nulo.");
        }

        // Validar que el turno sea un valor válido del enum
        try {
            Tipo_Turno.valueOf(bedelDtoRequest.getTurno().name());
        } catch (IllegalArgumentException e) {
            throw new InvalidBedelDataException("El tipo de turno no es válido.");
        }



    }

    private void validarPassword(String password) {
        if (password.length() < 6) {
            throw new InvalidBedelDataException("La contraseña debe tener al menos 6 caracteres.");
        }

        if (!password.matches(".*[@#$%&*].*")) {
            throw new InvalidBedelDataException("La contraseña debe contener al menos uno de los siguientes signos especiales: @#$%&*");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidBedelDataException("La contraseña debe contener al menos una letra mayúscula.");
        }

        if (!password.matches(".*\\d.*")) {
            throw new InvalidBedelDataException("La contraseña debe contener al menos un dígito.");
        }
    }




}

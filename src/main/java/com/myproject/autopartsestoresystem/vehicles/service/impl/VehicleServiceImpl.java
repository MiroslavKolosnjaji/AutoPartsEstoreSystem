package com.myproject.autopartsestoresystem.vehicles.service.impl;

import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.vehicles.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.vehicles.exception.VehicleNotFoundException;
import com.myproject.autopartsestoresystem.parts.mapper.PartMapper;
import com.myproject.autopartsestoresystem.vehicles.mapper.VehicleMapper;
import com.myproject.autopartsestoresystem.vehicles.entity.Vehicle;
import com.myproject.autopartsestoresystem.vehicles.repository.VehicleRepository;
import com.myproject.autopartsestoresystem.vehicles.search.specification.VehicleSpecification;
import com.myproject.autopartsestoresystem.vehicles.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final PartMapper partMapper;

    @Override
    public List<PartDTO> searchParts(String brand, String model, String series) {

        VehicleSpecification specification = VehicleSpecification.builder()
                .brand(brand)
                .model(model)
                .series(series)
                .build();

        List<Vehicle> vehicles = vehicleRepository.findAll(specification);

        return vehicles.stream()
                .flatMap(vehicle -> vehicle.getParts().stream())
                .map(partMapper::partToPartDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO save(VehicleDTO vehicleDTO) {

        Vehicle saved = vehicleRepository.save(vehicleMapper.vehicleDTOToVehicle(vehicleDTO));

        return vehicleMapper.vehicleToVehicleDTO(saved);
    }

    @Override
    public VehicleDTO update(Long id, VehicleDTO vehicleDTO) throws VehicleNotFoundException {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

        vehicle.setEngineType(vehicleDTO.getEngineType());
        vehicle.setSeries(vehicleDTO.getSeries());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setParts(vehicleDTO.getParts());

        Vehicle updated = vehicleRepository.save(vehicle);

        return vehicleMapper.vehicleToVehicleDTO(updated);
    }

    @Override
    public List<VehicleDTO> getAll() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::vehicleToVehicleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO getById(Long id) throws VehicleNotFoundException {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

        return vehicleMapper.vehicleToVehicleDTO(vehicle);
    }

    @Override
    public void delete(Long id) throws VehicleNotFoundException {

        if (!vehicleRepository.existsById(id))
            throw new VehicleNotFoundException("Vehicle not found");

        vehicleRepository.deleteById(id);
    }
}

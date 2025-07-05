package com.myproject.autopartsestoresystem.vehicles.mapper;

import com.myproject.autopartsestoresystem.vehicles.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.vehicles.entity.Vehicle;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface VehicleMapper {

    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);
    Vehicle vehicleDTOToVehicle(VehicleDTO vehicleDTO);
}

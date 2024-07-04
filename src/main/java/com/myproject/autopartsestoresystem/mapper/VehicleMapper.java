package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.model.Vehicle;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface VehicleMapper {

    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);
    Vehicle vehicleDTOToVehicle(VehicleDTO vehicleDTO);
}

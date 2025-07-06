package com.myproject.autopartsestoresystem.vehicles.controller;

import com.myproject.autopartsestoresystem.vehicles.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.vehicles.security.permission.VehicleCreatePermission;
import com.myproject.autopartsestoresystem.vehicles.security.permission.VehicleDeletePermission;
import com.myproject.autopartsestoresystem.vehicles.security.permission.VehicleReadPermission;
import com.myproject.autopartsestoresystem.vehicles.security.permission.VehicleUpdatePermission;
import com.myproject.autopartsestoresystem.vehicles.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    public static final String VEHICLE_URI = "/api/vehicle";
    public static final String VEHICLE_ID = "/{vehicleId}";
    public static final String VEHICLE_URI_WITH_ID = VEHICLE_URI + VEHICLE_ID;

    private final VehicleService vehicleService;

    @VehicleCreatePermission
    @PostMapping()
    public ResponseEntity<VehicleDTO> addVehicle(@Validated @RequestBody VehicleDTO vehicleDTO) throws EntityAlreadyExistsException {

        VehicleDTO saved = vehicleService.save(vehicleDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", VEHICLE_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @VehicleUpdatePermission
    @PutMapping(VEHICLE_ID)
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable("vehicleId") Long brandId, @Validated @RequestBody VehicleDTO vehicleDTO) throws EntityNotFoundException {

        VehicleDTO updated = vehicleService.update(brandId, vehicleDTO);
        return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);

    }

    @VehicleReadPermission
    @GetMapping()
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {

        List<VehicleDTO> vehicles = vehicleService.getAll();

        if (vehicles.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @VehicleReadPermission
    @GetMapping(VEHICLE_ID)
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("vehicleId") Long brandId) throws EntityNotFoundException {

        VehicleDTO vehicleDTO = vehicleService.getById(brandId);
        return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);

    }

    @VehicleDeletePermission
    @DeleteMapping(VEHICLE_ID)
    public ResponseEntity<Void> deleteVehicle(@PathVariable("vehicleId") Long brandId) throws EntityNotFoundException {

        vehicleService.delete(brandId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}

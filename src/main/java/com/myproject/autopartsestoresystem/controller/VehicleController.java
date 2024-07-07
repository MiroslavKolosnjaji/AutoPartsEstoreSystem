package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.VehicleNotFoundException;
import com.myproject.autopartsestoresystem.model.Vehicle;
import com.myproject.autopartsestoresystem.service.VehicleService;
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

    @PostMapping()
    public ResponseEntity<VehicleDTO> addVehicle(@Validated @RequestBody VehicleDTO vehicleDTO) {

        VehicleDTO saved = vehicleService.save(vehicleDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", VEHICLE_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(VEHICLE_ID)
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable("vehicleId") Long brandId,  @Validated @RequestBody VehicleDTO vehicleDTO) {

        try{

            VehicleDTO updated = vehicleService.update(brandId, vehicleDTO);
            return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);

        }catch (VehicleNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {

        List<VehicleDTO> vehicles = vehicleService.getAll();

        if(vehicles.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @GetMapping(VEHICLE_ID)
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("vehicleId") Long brandId) {

        try{

            VehicleDTO vehicleDTO = vehicleService.getById(brandId);
            return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);

        } catch (VehicleNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(VEHICLE_ID)
    public ResponseEntity<Void> deleteVehicle(@PathVariable("vehicleId") Long brandId) {

        try{
            vehicleService.delete(brandId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (VehicleNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

}
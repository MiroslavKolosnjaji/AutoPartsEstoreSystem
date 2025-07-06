package com.myproject.autopartsestoresystem.cities.controller;

import com.myproject.autopartsestoresystem.cities.dto.CityDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.cities.security.permission.CityCreatePermission;
import com.myproject.autopartsestoresystem.cities.security.permission.CityDeletePermission;
import com.myproject.autopartsestoresystem.cities.security.permission.CityReadPermission;
import com.myproject.autopartsestoresystem.cities.security.permission.CityUpdatePermission;
import com.myproject.autopartsestoresystem.cities.service.CityService;
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
@RequestMapping("/api/city")
@RequiredArgsConstructor
public class CityController {

    public static final String CITY_URI = "/api/city";
    public static final String CITY_ID = "/{city_id}";
    public static final String CITY_URI_WITH_ID = CITY_URI + CITY_ID;

    private final CityService cityService;

    @CityCreatePermission
    @PostMapping()
    public ResponseEntity<CityDTO> createCity(@Validated @RequestBody CityDTO cityDTO) throws EntityAlreadyExistsException {

            CityDTO saved = cityService.save(cityDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", CITY_URI + "/" + saved.getId());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @CityUpdatePermission
    @PutMapping(CITY_ID)
    public ResponseEntity<CityDTO> updateCity(@PathVariable("city_id") Long cityId, @Validated @RequestBody CityDTO cityDTO) throws EntityNotFoundException {

            cityService.update(cityId, cityDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @CityReadPermission
    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities() {

        List<CityDTO> cities = cityService.getAll();

        if (cities.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @CityReadPermission
    @GetMapping(CITY_ID)
    public ResponseEntity<CityDTO> getCity(@PathVariable("city_id") Long cityId) throws EntityNotFoundException {

            CityDTO cityDTO = cityService.getById(cityId);
            return new ResponseEntity<>(cityDTO, HttpStatus.OK);

    }

    @CityDeletePermission
    @DeleteMapping(CITY_ID)
    public ResponseEntity<Void> deleteCity(@PathVariable("city_id") Long cityId) throws EntityNotFoundException {

            cityService.delete(cityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}

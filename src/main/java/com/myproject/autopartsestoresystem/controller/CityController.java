package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.customer.CityDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.CityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.CityNotFoundException;
import com.myproject.autopartsestoresystem.service.CityService;
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

    @PostMapping()
    public ResponseEntity<CityDTO> createCity(@Validated @RequestBody CityDTO cityDTO) {

        try {

            CityDTO saved = cityService.save(cityDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", CITY_URI + "/" + saved.getId());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

        } catch (CityAlreadyExistsException e) {
            throw new EntityAlreadyExistsException(e.getMessage());
        }
    }

    @PutMapping(CITY_ID)
    public ResponseEntity<CityDTO> updateCity(@PathVariable("city_id") Long cityId, @Validated @RequestBody CityDTO cityDTO) {

        try {

            cityService.update(cityId, cityDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (CityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities() {

        List<CityDTO> cities = cityService.getAll();

        if (cities.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(CITY_ID)
    public ResponseEntity<CityDTO> getCity(@PathVariable("city_id") Long cityId) {

        try {

            CityDTO cityDTO = cityService.getById(cityId);
            return new ResponseEntity<>(cityDTO, HttpStatus.OK);

        } catch (CityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(CITY_ID)
    public ResponseEntity<CityDTO> deleteCity(@PathVariable("city_id") Long cityId) {

        try {

            cityService.delete(cityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (CityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

}

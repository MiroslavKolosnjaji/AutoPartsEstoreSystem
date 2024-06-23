package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.customer.BrandDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.BrandAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.BrandNotFoundException;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("api/brand")
@RequiredArgsConstructor
public class BrandController {


    public static final String BRAND_URI = "/api/brand";
    public static final String BRAND_ID = "/{brand_id}";
    public static final String BRAND_URI_WITH_ID = BRAND_URI + BRAND_ID;

    private final BrandService brandService;
    private final HandlerMapping resourceHandlerMapping;


    @PostMapping()
    public ResponseEntity<BrandDTO> createBrand(@Validated @RequestBody BrandDTO brandDTO) {
        try {
            BrandDTO saved = brandService.save(brandDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", BRAND_URI + "/" + saved.getId());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
        } catch (BrandAlreadyExistsException e) {
            throw new EntityAlreadyExistsException(e.getMessage());
        }
    }

    @PutMapping(BRAND_ID)
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable("brand_id") Long brandId, @Validated @RequestBody BrandDTO brandDTO) {
        try {

            brandService.update(brandId, brandDTO);
            return new ResponseEntity<>(brandDTO, HttpStatus.NO_CONTENT);

        }catch (BrandNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<BrandDTO>> getAllBrands() {

        List<BrandDTO> brands = brandService.getAll();

        if(brands.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping(BRAND_ID)
    public ResponseEntity<BrandDTO> getBrand(@PathVariable("brand_id") Long brandId) {

        try{

            BrandDTO brandDTO = brandService.getById(brandId);
            return new ResponseEntity<>(brandDTO, HttpStatus.OK);

        }catch (BrandNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(BRAND_ID)
    public ResponseEntity<BrandDTO> deleteBrand(@PathVariable("brand_id") Long brandId) {

        try{
            brandService.delete(brandId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (BrandNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }


}

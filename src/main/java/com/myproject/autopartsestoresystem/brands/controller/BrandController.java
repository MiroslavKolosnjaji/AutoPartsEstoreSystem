package com.myproject.autopartsestoresystem.brands.controller;

import com.myproject.autopartsestoresystem.brands.dto.BrandDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.brands.security.permission.BrandCreatePermission;
import com.myproject.autopartsestoresystem.brands.security.permission.BrandDeletePermission;
import com.myproject.autopartsestoresystem.brands.security.permission.BrandReadPermission;
import com.myproject.autopartsestoresystem.brands.security.permission.BrandUpdatePermission;
import com.myproject.autopartsestoresystem.brands.service.BrandService;
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
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {


    public static final String BRAND_URI = "/api/brand";
    public static final String BRAND_ID = "/{brand_id}";
    public static final String BRAND_URI_WITH_ID = BRAND_URI + BRAND_ID;

    private final BrandService brandService;


    @BrandCreatePermission
    @PostMapping()
    public ResponseEntity<BrandDTO> createBrand(@Validated @RequestBody BrandDTO brandDTO) throws EntityAlreadyExistsException {

        BrandDTO saved = brandService.save(brandDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", BRAND_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @BrandUpdatePermission
    @PutMapping(BRAND_ID)
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable("brand_id") Integer brandId, @Validated @RequestBody BrandDTO brandDTO) throws EntityNotFoundException {

        brandService.update(brandId, brandDTO);
        return new ResponseEntity<>(brandDTO, HttpStatus.NO_CONTENT);

    }

    @BrandReadPermission
    @GetMapping()
    public ResponseEntity<List<BrandDTO>> getAllBrands() {

        List<BrandDTO> brands = brandService.getAll();

        if (brands.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @BrandReadPermission
    @GetMapping(BRAND_ID)
    public ResponseEntity<BrandDTO> getBrand(@PathVariable("brand_id") Integer brandId) throws EntityNotFoundException {

        BrandDTO brandDTO = brandService.getById(brandId);
        return new ResponseEntity<>(brandDTO, HttpStatus.OK);

    }

    @BrandDeletePermission
    @DeleteMapping(BRAND_ID)
    public ResponseEntity<Void> deleteBrand(@PathVariable("brand_id") Integer brandId) throws EntityNotFoundException {

        brandService.delete(brandId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}

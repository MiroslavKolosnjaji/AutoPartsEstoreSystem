package com.myproject.autopartsestoresystem.models.controller;

import com.myproject.autopartsestoresystem.models.dto.ModelDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.models.entity.ModelId;
import com.myproject.autopartsestoresystem.security.permission.model.ModelCreatePermission;
import com.myproject.autopartsestoresystem.security.permission.model.ModelDeletePermission;
import com.myproject.autopartsestoresystem.security.permission.model.ModelReadPermission;
import com.myproject.autopartsestoresystem.security.permission.model.ModelUpdatePermission;
import com.myproject.autopartsestoresystem.models.service.ModelService;
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
@RequestMapping("api/model")
@RequiredArgsConstructor
public class ModelController {

    public static final String MODEL_URI = "/api/model";
    public static final String MODEL_ID = "/{brandId}/{name}";
    public static final String MODEL_URI_WITH_ID = MODEL_URI + MODEL_ID;

    private final ModelService modelService;

    @ModelCreatePermission
    @PostMapping()
    public ResponseEntity<ModelDTO> createModel(@Validated @RequestBody ModelDTO modelDTO) throws EntityAlreadyExistsException {

            ModelDTO saved = modelService.save(modelDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", MODEL_URI + "/" + saved.getId().getId() + "/" + saved.getId().getName());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @ModelUpdatePermission
    @PutMapping(MODEL_ID)
    public ResponseEntity<Void> updateModel(@PathVariable("brandId") Long brandId, @PathVariable("name") String name, @Validated @RequestBody ModelDTO modelDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

            modelService.update(new ModelId(brandId, name), modelDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @ModelReadPermission
    @GetMapping()
    public ResponseEntity<List<ModelDTO>> getAllModels() {

        List<ModelDTO> models = modelService.getAll();

        if (models.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @ModelReadPermission
    @GetMapping(MODEL_ID)
    public ResponseEntity<ModelDTO> getModel(@PathVariable("brandId") Long brandId, @PathVariable("name") String name) throws EntityNotFoundException {

            ModelDTO modelDTO = modelService.getById(new ModelId(brandId, name));
            return new ResponseEntity<>(modelDTO, HttpStatus.OK);

    }

    @ModelDeletePermission
    @DeleteMapping(MODEL_ID)
    public ResponseEntity<Void> deleteModel(@PathVariable("brandId") Long brandId, @PathVariable("name") String name) throws EntityNotFoundException {

            modelService.delete(new ModelId(brandId, name));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}

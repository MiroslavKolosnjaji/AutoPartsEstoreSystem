package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.customer.ModelDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.ModelAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.ModelNotFoundException;
import com.myproject.autopartsestoresystem.model.ModelId;
import com.myproject.autopartsestoresystem.service.ModelService;
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

    @PostMapping()
    public ResponseEntity<ModelDTO> createModel(@Validated @RequestBody ModelDTO modelDTO) {

        try {

            ModelDTO saved = modelService.save(modelDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", MODEL_URI + "/" + saved.getId().getId() + "/" + saved.getId().getName());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
        } catch (ModelAlreadyExistsException e) {
            throw new EntityAlreadyExistsException(e.getMessage());
        }
    }

    @PutMapping(MODEL_ID)
    public ResponseEntity<Void> updateModel(@PathVariable("brandId") Long brandId, @PathVariable("name") String name, @Validated @RequestBody ModelDTO modelDTO) {

        try {

            modelService.update(new ModelId(brandId, name), modelDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (ModelNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<ModelDTO>> getAllModels() {

        List<ModelDTO> models = modelService.getAll();

        if (models.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @GetMapping(MODEL_ID)
    public ResponseEntity<ModelDTO> getModel(@PathVariable("brandId") Long brandId, @PathVariable("name") String name) {

        try {

            ModelDTO modelDTO = modelService.getById(new ModelId(brandId, name));
            return new ResponseEntity<>(modelDTO, HttpStatus.OK);

        } catch (ModelNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(MODEL_ID)
    public ResponseEntity<Void> deleteModel(@PathVariable("brandId") Long brandId, @PathVariable("name") String name) {

        try{

            modelService.delete(new ModelId(brandId, name));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (ModelNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }


}

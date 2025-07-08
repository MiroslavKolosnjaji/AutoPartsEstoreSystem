package com.myproject.autopartsestoresystem.models.controller;

import com.myproject.autopartsestoresystem.models.dto.ModelDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.models.security.permission.ModelCreatePermission;
import com.myproject.autopartsestoresystem.models.security.permission.ModelDeletePermission;
import com.myproject.autopartsestoresystem.models.security.permission.ModelReadPermission;
import com.myproject.autopartsestoresystem.models.security.permission.ModelUpdatePermission;
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
@RequestMapping("api/models")
@RequiredArgsConstructor
public class ModelController {

    public static final String MODEL_URI = "/api/models";
    public static final String MODEL_ID = "/{id}";
    public static final String MODEL_URI_WITH_ID = MODEL_URI + MODEL_ID;

    private final ModelService modelService;

    @ModelCreatePermission
    @PostMapping()
    public ResponseEntity<ModelDTO> createModel(@Validated @RequestBody ModelDTO modelDTO) throws EntityAlreadyExistsException {

            ModelDTO saved = modelService.save(modelDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", MODEL_URI + "/" + saved.getId());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @ModelUpdatePermission
    @PutMapping(MODEL_ID)
    public ResponseEntity<Void> updateModel(@PathVariable("id") Integer id, @Validated @RequestBody ModelDTO modelDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

            modelService.update(id, modelDTO);
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
    public ResponseEntity<ModelDTO> getModel(@PathVariable("id") Integer id) throws EntityNotFoundException {

            ModelDTO modelDTO = modelService.getById(id);
            return new ResponseEntity<>(modelDTO, HttpStatus.OK);

    }

    @ModelDeletePermission
    @DeleteMapping(MODEL_ID)
    public ResponseEntity<Void> deleteModel(@PathVariable("id") Integer id) throws EntityNotFoundException {

            modelService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}

package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PartAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.PartNotFoundException;
import com.myproject.autopartsestoresystem.service.PartService;
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
@RequestMapping("api/part")
@RequiredArgsConstructor
public class PartController {

    public static final String PART_URI = "/api/part";
    public static final String PART_ID = "/{partId}";
    public static final String PART_URI_WITH_ID = PART_URI + PART_ID;

    private final PartService partService;

    @PostMapping()
    public ResponseEntity<PartDTO> addPart(@Validated @RequestBody PartDTO partDTO) {

        try{

           PartDTO saved = partService.save(partDTO);

           HttpHeaders responseHeaders = new HttpHeaders();
           responseHeaders.add(HttpHeaders.LOCATION, PART_URI + "/" + saved.getId());

           return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

        }catch (PartAlreadyExistsException e){
            throw new EntityAlreadyExistsException(e.getMessage());
        }
    }


    @PutMapping(PART_ID)
    private ResponseEntity<Void> updatePart(@PathVariable("partId") Long id, @Validated @RequestBody PartDTO partDTO) {

        try{

            partService.update(id, partDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (PartNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<PartDTO>> getParts() {

        List<PartDTO> parts = partService.getAll();

        if(parts.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(parts, HttpStatus.OK);

    }

    @GetMapping(PART_ID)
    public ResponseEntity<PartDTO> getPart(@PathVariable("partId") Long id) {

        try{

            PartDTO partDTO = partService.getById(id);
            return new ResponseEntity<>(partDTO, HttpStatus.OK);

        }catch (PartNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(PART_ID)
    public ResponseEntity<Void> deletePart(@PathVariable("partId") Long id) {

        try{

            partService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (PartNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}

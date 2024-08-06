package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.model.Store;
import com.myproject.autopartsestoresystem.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    public static final String STORE_URI = "/api/store";
    public static final String STORE_ID = "/{storeId}";
    public static final String STORE_URI_WITH_ID = STORE_URI + STORE_ID;

    private final StoreService storeService;

    @PostMapping()
    public ResponseEntity<StoreDTO> saveStore(@Valid @RequestBody StoreDTO storeDTO) {

        StoreDTO saved = storeService.save(storeDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.LOCATION, STORE_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(STORE_ID)
    public ResponseEntity<StoreDTO> updateStore(@PathVariable("storeId") Long storeId, @Valid @RequestBody StoreDTO storeDTO) throws EntityNotFoundException {

        StoreDTO updated = storeService.update(storeId, storeDTO);

        return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);
    }

    @GetMapping(STORE_ID)
    public ResponseEntity<StoreDTO> getStore(@PathVariable("storeId") Long storeId) throws EntityNotFoundException {

        StoreDTO storeDTO = storeService.getById(storeId);
        return new ResponseEntity<>(storeDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {

        List<StoreDTO> storeDTOS = storeService.getAll();

        if (storeDTOS.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(storeDTOS, HttpStatus.OK);
    }

    @DeleteMapping(STORE_ID)
    public ResponseEntity<Void> deleteStore(@PathVariable("storeId") Long storeId) throws EntityNotFoundException {

        storeService.delete(storeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}



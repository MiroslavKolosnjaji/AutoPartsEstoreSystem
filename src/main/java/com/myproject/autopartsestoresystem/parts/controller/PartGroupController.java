package com.myproject.autopartsestoresystem.parts.controller;

import com.myproject.autopartsestoresystem.parts.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.security.permission.partgroup.PartGroupCreatePermission;
import com.myproject.autopartsestoresystem.security.permission.partgroup.PartGroupDeletePermission;
import com.myproject.autopartsestoresystem.security.permission.partgroup.PartGroupReadPermission;
import com.myproject.autopartsestoresystem.security.permission.partgroup.PartGroupUpdatePermission;
import com.myproject.autopartsestoresystem.parts.service.PartGroupService;
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
@RequestMapping("api/partGroup")
@RequiredArgsConstructor
public class PartGroupController {

    public static final String PARTGROUP_URI = "/api/partGroup";
    public static final String PARTGROUP_ID = "/{partGroupId}";
    public static final String PARTGROUP_URI_WITH_ID = PARTGROUP_URI + PARTGROUP_ID;

    private final PartGroupService partGroupService;

    @PartGroupCreatePermission
    @PostMapping()
    public ResponseEntity<PartGroupDTO> createPartGroup(@Validated @RequestBody PartGroupDTO partGroupDTO) throws EntityAlreadyExistsException {

        PartGroupDTO saved = partGroupService.save(partGroupDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.LOCATION, PARTGROUP_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @PartGroupUpdatePermission
    @PutMapping(PARTGROUP_ID)
    public ResponseEntity<PartGroupDTO> updatePartGroup(@PathVariable("partGroupId") Long id, @Validated @RequestBody PartGroupDTO partGroupDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        partGroupService.update(id, partGroupDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PartGroupReadPermission
    @GetMapping()
    public ResponseEntity<List<PartGroupDTO>> getAllPartGroups() {

        List<PartGroupDTO> partgroups = partGroupService.getAll();

        if (partgroups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(partgroups, HttpStatus.OK);

    }


    @PartGroupReadPermission
    @GetMapping(PARTGROUP_ID)
    public ResponseEntity<PartGroupDTO> getPartGroup(@PathVariable("partGroupId") Long id) throws EntityNotFoundException {

        PartGroupDTO partGroupDTO = partGroupService.getById(id);
        return new ResponseEntity<>(partGroupDTO, HttpStatus.OK);

    }

    @PartGroupDeletePermission
    @DeleteMapping(PARTGROUP_ID)
    public ResponseEntity<Void> deletePartGroup(@PathVariable("partGroupId") Long id) throws EntityNotFoundException {

        partGroupService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}

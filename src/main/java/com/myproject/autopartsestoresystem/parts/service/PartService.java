package com.myproject.autopartsestoresystem.parts.service;

import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.parts.exception.PartNotFoundException;
import com.myproject.autopartsestoresystem.service.CrudService;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PartService extends CrudService<PartDTO, Long> {

    List<PartDTO> getSelectedParts(List<Long> selectedPartIds) throws PartNotFoundException;
}

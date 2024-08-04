package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.exception.service.PartNotFoundException;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PartService extends CrudService<PartDTO, Long> {

    List<PartDTO> getSelectedParts(List<Long> selectedPartIds) throws PartNotFoundException;
}

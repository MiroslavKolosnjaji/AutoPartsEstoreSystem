package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.dto.VehicleDTO;

import java.util.List;


/**
 * @author Miroslav Kološnjaji
 */
public interface VehicleService extends CrudService<VehicleDTO, Long> {

    List<PartDTO> searchParts(String brand, String model, String series);
}

package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.CardDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CardService extends CrudService<CardDTO, Long> {

    List<CardDTO> getCardsByHolderName(String holderName);
}

package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.model.Card;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface CardMapper {

    CardDTO cardToCardDTO(Card card);
    Card cardDTOToCard(CardDTO cardDTO);
}

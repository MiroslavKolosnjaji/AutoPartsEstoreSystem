package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "customer.id", target = "customerId")
    CardDTO cardToCardDTO(Card card);

    @Mapping(target = "customer.id", source = "customerId")
    Card cardDTOToCard(CardDTO cardDTO);
}

package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.exception.service.CardNotFoundException;
import com.myproject.autopartsestoresystem.mapper.CardMapper;
import com.myproject.autopartsestoresystem.model.Card;
import com.myproject.autopartsestoresystem.repository.CardRepository;
import com.myproject.autopartsestoresystem.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final TextEncryptor textEncryptor;


    @Override
    public CardDTO save(CardDTO cardDTO) {

        Optional<Card> foundedCard = cardRepository.findByCardNumber(encrypt(cardDTO.getCardHolder()));

        if(foundedCard.isPresent())
            return cardMapper.cardToCardDTO(foundedCard.get());

        cardDTO.setCardNumber(encrypt(cardDTO.getCardNumber()));
        cardDTO.setCvv(null);

        Card saved = cardRepository.save(cardMapper.cardDTOToCard(cardDTO));

        return cardMapper.cardToCardDTO(saved);
    }

    @Override
    public CardDTO update(Long id, CardDTO cardDTO) {

        Card foundedCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        foundedCard.setCardNumber(encrypt(cardDTO.getCardHolder()));
        foundedCard.setCardHolder(cardDTO.getCardHolder());
        foundedCard.setExpiryDate(cardDTO.getExpiryDate());
        foundedCard.setCustomer(cardDTO.getCustomer());

        Card updated = cardRepository.save(foundedCard);

        return cardMapper.cardToCardDTO(updated);
    }

    @Override
    public List<CardDTO> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CardDTO getById(Long id) {
        return cardRepository.findById(id)
                .map(cardMapper::cardToCardDTO)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    @Override
    public void delete(Long id) {

        if(!cardRepository.existsById(id))
            throw new CardNotFoundException("Card not found");

        cardRepository.deleteById(id);
    }

    @Override
    public List<CardDTO> getCardsByHolderName(String holderName) {
        return cardRepository.findByCardHolder(holderName).stream()
                .map(cardMapper::cardToCardDTO)
                .collect(Collectors.toList());

    }

    private String encrypt(String sensitiveData){
        return textEncryptor.encrypt(sensitiveData);
    }

    private String decrypt(String sensitiveData){
        return textEncryptor.decrypt(sensitiveData);
    }
}

package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.CardNotFoundException;
import com.myproject.autopartsestoresystem.mapper.CardMapper;
import com.myproject.autopartsestoresystem.model.Card;
import com.myproject.autopartsestoresystem.customers.entity.Customer;
import com.myproject.autopartsestoresystem.repository.CardRepository;
import com.myproject.autopartsestoresystem.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public CardDTO save(CardDTO cardDTO) throws EntityAlreadyExistsException {

        Optional<Card> foundedCard = cardRepository.findByCardNumber(encrypt(cardDTO.getCardNumber()));

        if (foundedCard.isPresent()) {
            return cardMapper.cardToCardDTO(foundedCard.get());
        }

        Card card = cardMapper.cardDTOToCard(cardDTO);

        card.setCardNumber(encrypt(cardDTO.getCardNumber()));
        card.setCvv(null);

        Card saved = cardRepository.save(card);

        saved.setCardNumber(decrypt(saved.getCardNumber()));

        return cardMapper.cardToCardDTO(saved);
    }

    @Override
    @Transactional
    public CardDTO update(Long id, CardDTO cardDTO) throws CardNotFoundException {

        Card foundedCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));


        foundedCard.setCardNumber(encrypt(cardDTO.getCardNumber()));
        foundedCard.setCardHolder(cardDTO.getCardHolder());
        foundedCard.setExpiryDate(cardDTO.getExpiryDate());
        foundedCard.setCustomer(Customer.builder().id(cardDTO.getCustomerId()).build());

        Card updated = cardRepository.save(foundedCard);

        updated.setCardNumber(decrypt(updated.getCardNumber()));

        return cardMapper.cardToCardDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public CardDTO getById(Long id) throws CardNotFoundException {
        return cardRepository.findById(id)
                .map(card -> {
                    card.setCardNumber(decrypt(card.getCardNumber()));
                    return cardMapper.cardToCardDTO(card);
                })
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    @Override
    public void delete(Long id) throws CardNotFoundException {

        if (!cardRepository.existsById(id))
            throw new CardNotFoundException("Card not found");

        cardRepository.deleteById(id);
    }

    @Override
    public List<CardDTO> getCardsByHolderName(String holderName) {
        return cardRepository.findByCardHolder(holderName).stream()
                .map(card -> {

                    card.setCardNumber(decrypt(card.getCardNumber()));
                    return cardMapper.cardToCardDTO(card);
                })
                .collect(Collectors.toList());

    }

    private String encrypt(String sensitiveData) {
        return textEncryptor.encrypt(sensitiveData);
    }

    private String decrypt(String encryptedData) {
        return textEncryptor.decrypt(encryptedData);
    }

}

package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.service.CardNotFoundException;
import com.myproject.autopartsestoresystem.mapper.CardMapper;
import com.myproject.autopartsestoresystem.model.Card;
import com.myproject.autopartsestoresystem.model.Customer;
import com.myproject.autopartsestoresystem.repository.CardRepository;
import com.myproject.autopartsestoresystem.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TextEncryptor textEncryptor;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private CardDTO cardDTO;


    @BeforeEach
    void setUp() {
        cardDTO = CardDTO.builder()
                .cardHolder("John Smith")
                .cardNumber("5528637386855499")
                .expiryDate(LocalDate.of(2025,12, 1))
                .cvv("123")
                .customerId(1L)
                .build();
    }

    @DisplayName("Save Card")
    @Test
    void testSaveCard_whenValidDetailsProvided_returnsCardDTO() {

        //given
        Card card = new Card();
        cardDTO.setCardNumber("encrypted_" + cardDTO.getCardNumber());


        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.empty());
        when(cardMapper.cardToCardDTO(card)).thenReturn(cardDTO);
        when(cardMapper.cardDTOToCard(cardDTO)).thenReturn(card);
        when(textEncryptor.encrypt(anyString())).thenAnswer(invocation -> "encrypted_" + invocation.getArgument(0));
        when(textEncryptor.decrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0).toString().replace("encrypted_", ""));

        when(cardRepository.save(card)).thenReturn(card);

        //when
        CardDTO savedDTO = cardService.save(cardDTO);

        //then
        assertNotNull(savedDTO, "Save Card should not be null");
        assertEquals(cardDTO.getCardNumber(), savedDTO.getCardNumber(), "Card Number should be equal");

        verify(cardRepository).findByCardNumber(anyString());
        verify(cardMapper).cardToCardDTO(card);
        verify(cardMapper).cardDTOToCard(cardDTO);
        verify(textEncryptor, times(2)).encrypt(anyString());
        verify(textEncryptor).decrypt(anyString());
        verify(cardRepository).save(card);
    }

    @DisplayName("Save Card - Not Saved Because Already Exists")
    @Test
    void saveCard_whenCardAlreadyExists_returnsCardDTO() {

        //given
        Card card = mock(Card.class);
        cardDTO.setCardNumber("encrypted_" + cardDTO.getCardNumber());

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.of(card));
        when(textEncryptor.encrypt(anyString())).thenAnswer(invocation -> "encrypted_" + invocation.getArgument(0));
        when(cardMapper.cardToCardDTO(card)).thenReturn(cardDTO);

        //when
        CardDTO savedDTO = cardService.save(cardDTO);

        //then
        assertNotNull(savedDTO, "Saved card should not be null");

        verify(cardRepository).findByCardNumber(anyString());
        verify(cardMapper).cardToCardDTO(card);
    }

    @DisplayName("Update Card")
    @Test
    void testUpdateCard_whenValidDetailsProvided_returnsUpdatedDTO() {

        //given
        Card card = new Card();
        cardDTO.setCardNumber("encrypted_" + cardDTO.getCardNumber());

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(cardMapper.cardToCardDTO(card)).thenReturn(cardDTO);
        when(textEncryptor.encrypt(anyString())).thenAnswer(invocation -> "encrypted_" + invocation.getArgument(0));
        when(textEncryptor.decrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0).toString().replace("encrypted_", ""));

        when(cardRepository.save(card)).thenReturn(card);

        //when
        CardDTO updatedDTO = cardService.update(anyLong(), cardDTO);

        assertNotNull(updatedDTO, "Update card should not be null");
        assertEquals(cardDTO.getCardNumber(), updatedDTO.getCardNumber(), "Card Number should be equal");

        verify(cardRepository).findById(anyLong());
        verify(cardMapper).cardToCardDTO(card);
        verify(textEncryptor, times(1)).encrypt(anyString());
        verify(textEncryptor).decrypt(anyString());
        verify(cardRepository).save(card);
    }

    @DisplayName("Update Card Failed - Invalid ID Provided")
    @Test
    void testUpdateCard_whenInvalidIdProvided_throwsCardNotFoundException() {

        //given
        when(cardRepository.findById(anyLong())).thenThrow(CardNotFoundException.class);

        //when
        Executable executable = () -> cardService.update(anyLong(), cardDTO);

        //then
        assertThrows(CardNotFoundException.class, executable, "Exception does not match. Expected CardNotFoundException");
    }

    @DisplayName("Get Card By ID")
    @Test
    void testGetCardById_whenValidIdProvided_returnsCardDTO() {

        //given
        Card card = new Card();
        card.setCardNumber("encrypted_" + cardDTO.getCardNumber());

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(cardMapper.cardToCardDTO(card)).thenReturn(cardDTO);
        when(textEncryptor.decrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0).toString().replace("encrypted_", ""));

        //when
        CardDTO foundDTO = cardService.getById(anyLong());

        assertNotNull(foundDTO, "Found card should not be null");
        assertEquals(16, foundDTO.getCardNumber().length(), "Length of card number should be 16");

        verify(cardRepository).findById(anyLong());
        verify(cardMapper).cardToCardDTO(card);
        verify(textEncryptor).decrypt(anyString());

    }

    @DisplayName("Get Card By ID Failed - Invalid ID Provided")
    @Test
    void testGetCardById_whenInvalidIdProvided_throwsCardNotFoundException() {

        //given
        when(cardRepository.findById(anyLong())).thenThrow(CardNotFoundException.class);

        //when
        Executable executable = () -> cardService.getById(anyLong());

        //then
        assertThrows(CardNotFoundException.class, executable, "Exception does not match. Expected CardNotFoundException");
    }

    @DisplayName("Get Card By Holder Name")
    @Test
    void testGetCardByHolderName_whenHolderNameIsValid_returnsCardDTO() {

        //given
        Card card = new Card();
        card.setCardNumber("encrypted_" + cardDTO.getCardNumber());

        when(cardRepository.findByCardHolder(anyString())).thenReturn(List.of(card));
        when(cardMapper.cardToCardDTO(any(Card.class))).thenReturn(cardDTO);
        when(textEncryptor.decrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0).toString().replace("encrypted_", ""));

        //when
        List<CardDTO> cardDTOList = cardService.getCardsByHolderName(anyString());

        //then
        assertNotNull(cardDTOList, "List should not be null");
        assertEquals(1, cardDTOList.size(),"Expected 1 element in a list");

        verify(cardRepository).findByCardHolder(anyString());
        verify(cardMapper).cardToCardDTO(any(Card.class));
        verify(textEncryptor).decrypt(anyString());
    }

    @DisplayName("Delete Card By ID")
    @Test
    void testDeleteCardById_whenValidIdProvided_thenCorrect() {

        //given
        when(cardRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(cardRepository).deleteById(anyLong());

        //when
        cardService.delete(anyLong());

        //then
        verify(cardRepository).deleteById(anyLong());
        verify(cardRepository).existsById(anyLong());
    }

    @DisplayName("Delete Card By ID Failed - Invalid ID Provided")
    @Test
    void testDeleteCardById_whenInvalidIdProvided_throwCardNotFoundException() {

        //given
        doThrow(CardNotFoundException.class).when(cardRepository).existsById(anyLong());

        //when
        Executable executable = () -> cardService.delete(anyLong());

        //then
        assertThrows(CardNotFoundException.class, executable, "Exception does not match. Expected CardNotFoundException");
    }
}
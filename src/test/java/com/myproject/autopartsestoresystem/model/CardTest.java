package com.myproject.autopartsestoresystem.model;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class CardTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        Card card1 = Card.builder()
                .id(1L)
                .cardHolder("John Smith")
                .cardNumber("5528637386855499")
                .expiryDate(LocalDate.of(2025,12, 1))
                .cvv("123")
                .customer(null)
                .build();

        Card card2 = Card.builder()
                .id(1L)
                .cardHolder("John Smith")
                .cardNumber("5528637386855499")
                .expiryDate(LocalDate.of(2025,12, 1))
                .cvv("123")
                .customer(null)
                .build();

        boolean equals = card1.equals(card2);
        boolean hashCode = card1.hashCode() == card2.hashCode();


        //when & then
        assertAll("Card fields validation",
                () -> assertEquals(card1.getId(), card2.getId(), "Card id mismatch"),
                () -> assertEquals(card1.getCardNumber(), card2.getCardNumber(), "Card number mismatch"),
                () -> assertEquals(card1.getCardHolder(), card2.getCardHolder(), "Card holder mismatch"),
                () -> assertEquals(card1.getExpiryDate(), card2.getExpiryDate(), "Card expiry date mismatch"),
                () -> assertEquals(card1.getCvv(), card2.getCvv(), "Card cvv mismatch"),
                () -> assertEquals(card1.getCustomer(), card2.getCustomer(), "Card customer mismatch"));

        assertTrue(equals, "Card1 does not equal to card2");
        assertTrue(hashCode, "Card1 hash code does not equal to card2 hash code");
    }
}
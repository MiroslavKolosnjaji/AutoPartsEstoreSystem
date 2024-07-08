package com.myproject.autopartsestoresystem.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class CardDTOTest {


    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<CardDTO>> violations;
    private ConstraintViolation<CardDTO> violation;
    private CardDTO cardDTO;

    public CardDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }


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

    @Test
    void testCardNumberValidation_whenValidCardNumberProvided_thenCorrect() {

        //given
        cardDTO.setCardNumber("5528637386855499");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid card number");
    }

    @Test
    void testCardNumberValidation_whenInvalidCardNumberProvided_thenValidationFailed() {

        //given
        cardDTO.setCardNumber("111122223333442312");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for invalid card number");

        violation = violations.iterator().next();
        assertEquals("Invalid credit card number", violation.getMessage());
    }

    @Test
    void testCardNumberValidation_whenCardNumberIsEmpty_thenValidationFailed() {

        //given
        cardDTO.setCardNumber("");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for empty card number");

        violation = violations.iterator().next();
        assertEquals("Invalid credit card number", violation.getMessage());
    }

    @Test
    void testCardHolderValidation_whenCardHolderIsNotEmpty_thenCorrect() {

        //given
        cardDTO.setCardHolder("John Smith");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid card holder");
    }

    @Test
    void testCardHolderValidation_whenCardHolderIsEmpty_thenValidationFailed() {

        //given
        cardDTO.setCardHolder("");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for empty card holder");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Card holder name cannot be empty");
    }

    @Test
    void testCardHolderValidation_whenCardHolderIsLongerThan125Characters_thenValidationFailed() {

        //given
        cardDTO.setCardHolder("alksjdfhlasdkjfhgahjkfgvnblkzhjvlkcvjhzlkvjhzxvlkjxhvlkvjhlkzjhvzlkjvhvkjhaslkjfhdslfkjahsdfkjashsadfasdfasfasfasfdasfsdfkasjdfhgaksdfgwequiryww");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for card holder that is longer than 125 characters");

        violation = violations.iterator().next();
        assertEquals("Card holder name cannot be longer than 125 characters", violation.getMessage());
    }

    @Test
    void testExpiryDate_whenExpiryDateIsNotNull_thenCorrect() {

        //given
        cardDTO.setExpiryDate(LocalDate.now());

        //when
        violations = validator.validate(cardDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid expiry date");
    }

    @Test
    void testExpiryDate_whenExpiryDateIsNull_thenCorrect() {

        //given
        cardDTO.setExpiryDate(null);

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for expiry date that is null");

        violation = violations.iterator().next();
        assertEquals("Expiry date cannot be null", violation.getMessage());
    }

    @Test
    void testCvvValidation_WhenCvvWith3DigitsProvided_thenCorrect() {

        //given
        cardDTO.setCvv("123");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid cvv");
    }

    @Test
    void testCvvValidation_WhenCvvWith4DigitsProvided_thenCorrect() {

        //given
        cardDTO.setCvv("1234");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid cvv");
    }

    @Test
    void testCvvValidation_WhenInvalidCvvProvided_thenValidationFailed() {

        //given
        cardDTO.setCvv("12345");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for invalid cvv");

        violation = violations.iterator().next();
        assertEquals("Invalid CVV", violation.getMessage());
    }

    @Test
    void testCvvValidation_WhenCvvIsEmpty_thenValidationFailed() {

        //given
        cardDTO.setCvv("");

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for invalid cvv");

        violation = violations.iterator().next();
        assertEquals( "Invalid CVV", violation.getMessage());
    }

    @Test
    void testCustomerIdValidation_whenCustomerIdIsNotNull_thenCorrect() {

        //given
        cardDTO.setCustomerId(1L);

        //when
        violations = validator.validate(cardDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid customer id");
    }

    @Test
    void testCustomerIdValidation_whenCustomerIdIsNull_thenValidationFailed() {

        //given
        cardDTO.setCustomerId(null);

        //when
        violations = validator.validate(cardDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for customer id that is null");

        violation = violations.iterator().next();
        assertEquals("Customer ID cannot be null", violation.getMessage());
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWIthSameDetails_thenIsEqual() {

        //given
       CardDTO cardDTO2 = CardDTO.builder()
               .cardHolder("John Smith")
               .cardNumber("5528637386855499")
               .expiryDate(LocalDate.of(2025,12, 1))
               .cvv("123")
               .customerId(1L)
               .build();

        boolean isEqual = cardDTO2.equals(cardDTO);
        boolean isEqualHashCode = cardDTO2.hashCode() == cardDTO.hashCode();

        assertTrue(isEqual, "CardDTO should be equal");
        assertTrue(isEqualHashCode, "CardDTO hashCode should be equal");
    }
}
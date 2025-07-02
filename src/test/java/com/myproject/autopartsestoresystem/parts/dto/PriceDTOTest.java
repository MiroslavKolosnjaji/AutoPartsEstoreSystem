package com.myproject.autopartsestoresystem.parts.dto;

import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.parts.entity.PriceId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PriceDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PriceDTO>> violations;
    private ConstraintViolation<PriceDTO> violation;
    PriceDTO priceDTO;

    public PriceDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        priceDTO = PriceDTO.builder()
                .id(new PriceId(1L, 1L))
                .price(new BigDecimal("125.99"))
                .currency(Currency.USD)
                .dateModified(LocalDateTime.now())
                .build();
    }

    @DisplayName("ID - Valid Input")
    @Test
    void testPriceId_whenValidInputProvided_thenCorrect() {

        //given
        priceDTO.setId(new PriceId(1L, 2L));

        //when
        violations = validator.validate(priceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid ID");
    }

    @DisplayName("ID - Invalid Input -ID Is NULL")
    @Test
    void testPriceId_whenInvalidInputProvided_thenValidationFailed() {

        //given
        priceDTO.setId(null);

        //when
        violations = validator.validate(priceDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(), "Violation should fail for ID that is NULL");
        assertTrue(violation.getMessage().contains("Price ID cannot be null"));
    }

    @DisplayName("Price - Valid Input")
    @Test
    void testPrice_whenValidInputProvided_thenCorrect() {

        //given
        priceDTO.setPrice(new BigDecimal("545.99"));

        //when
        violations = validator.validate(priceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid price");
    }

    @DisplayName("Price - Invalid Input - Price Is NULL")
    @Test
    void testPrice_whenPriceIsNull_thenValidationFailed() {

        //given
        priceDTO.setPrice(null);

        //when
        violations = validator.validate(priceDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(), "Violation should fail for price that is NULL");
        assertTrue(violation.getMessage().contains("Price cannot be null"));
    }

    @DisplayName("Price - Invalid Input - Price Have More Than 11 Digits")
    @Test
    void testPrice_WhenPriceHaveMoreThan11Digits_thenValidationFailed() {

        //given
        priceDTO.setPrice(new BigDecimal("545535766989.99"));

        //when
        violations = validator.validate(priceDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(), "Violation should fail for price that has more than 11 digits");
        assertTrue(violation.getMessage().contains("Price must have up to 11 digits in the integer part and up to 2 digits in the fraction part"));
    }

    @DisplayName("Price - Invalid Input - Negative Number")
    @Test
    void testPrice_whenNegativePriceProvided_thenValidationFailed() {

        //given
        priceDTO.setPrice(new BigDecimal("-1"));

        //when
        violations = validator.validate(priceDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(), "Violation should fail for negative price");
        assertTrue(violation.getMessage().contains("Price cannot be negative"));
    }

    @DisplayName("Currency - Valid Input")
    @Test
    void testCurrency_whenValidInputProvided_thenCorrect() {

        //given
        priceDTO.setCurrency(Currency.USD);

        //when
        violations = validator.validate(priceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid currency");
    }

    @DisplayName("Currency - Invalid Input - Currency Is Null")
    @Test
    void testCurrency_whenCurrencyIsNull_thenValidationFailed() {

        //given
        priceDTO.setCurrency(null);

        //when
        violations = validator.validate(priceDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(), "Violation should fail for valid currency");
        assertTrue(violation.getMessage().contains("Currency cannot be null"));
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        PriceDTO priceDTO2 = PriceDTO.builder()
                .id(new PriceId(1L, 1L))
                .price(new BigDecimal("125.99"))
                .currency(Currency.USD)
                .dateModified(priceDTO.getDateModified())
                .build();

        //when
        boolean isEqual = priceDTO2.equals(priceDTO);
        boolean isEqualHashCode = priceDTO2.hashCode() == priceDTO.hashCode();

        assertTrue(isEqual, "PriceDTO should be equal");
        assertTrue(isEqualHashCode, "PriceDTO hashCode should be equal");
    }
}

package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author Miroslav Kološnjaji
 */
class PartDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PartDTO>> violations;
    private ConstraintViolation<PartDTO> violation;
    private PartDTO partDTO;

    public PartDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {

        List<Price> prices = new ArrayList<>();
        prices.add(Price.builder().id(new PriceId(1L, 0L))
                .price(new BigDecimal("69.99"))
                .currency(Currency.USD)
                .dateModified(LocalDateTime.now())
                .build());

        partDTO = PartDTO.builder()
                .id(1L)
                .partNumber("BR56789")
                .partName("Rear Brake Rotor")
                .description("High-quality rear brake rotor designed for optimal heat dissapation and minimal wear. Ensures smooth and effective braking perfomance")
                .prices(prices)
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build())
                .vehicles(new ArrayList<>())
                .build();
    }

    @DisplayName("Part Number - Valid Input")
    @Test
    void testPartNumberValidation_whenValidInputProvided_thenCorrect() {

        //given
        partDTO.setPartNumber("BR56999");

        //when
        violations = validator.validate(partDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid part number");
    }

    @DisplayName("Part Number - Invalid Input - empty field")
    @Test
    void testPartNumberValidation_whenInvalidInputProvided_thenValidationFailed() {

        //given
        partDTO.setPartNumber("");

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for part number that is empty");

        violation = violations.iterator().next();
        assertEquals("Part number cannot be blank", violation.getMessage());
    }

    @DisplayName("Part Number - Invalid Input - Part Number Longer Than 20 Characters")
    @Test
    void testPartNumberValidation_whenPartNumberIsLongerThan20Characters_thenValidationFailed() {

        //given
        partDTO.setPartNumber("asdkjfhaskdjfhaksdjfhasdfkjh");

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for part number that is longer than 20 characters");

        violation = violations.iterator().next();
        assertEquals("Part number cannot be longer than 20 characters", violation.getMessage());
    }

    @DisplayName("Part Name - Valid Input")
    @Test
    void testPartName_whenValidInputProvided_thenCorrect() {

        //given
        partDTO.setPartName("Brake Rotor");

        //when
        violations = validator.validate(partDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for part name");
    }

    @DisplayName("Part Name - Invalid Input - Part Name Is Empty")
    @Test
    void testPartName_whenPartNameIsEmpty_thenValidationFailed() {

        //given
        partDTO.setPartName("");

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty part name");

        violation = violations.iterator().next();
        assertEquals("Part name cannot be blank", violation.getMessage());
    }

    @DisplayName("Part Name - Invalid Input - Part Name Is Longer Than 120 Characters")
    @Test
    void testPartName_whenPartNameIsLongerThan120Characters_thenValidationFailed() {

        //given
        partDTO.setPartName("alkfhdjlhfdjakalhjkfdhjlhjkaklkhjafhkjflkjhadsfhasdlkfahsdlfkjasdhflasdkjhflasdkjfhasdlkfjhasdflkjasdhflasdjkfhasdlkjfhasdlkjfhasdljkfhasdl");

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for part name that is longer than 120 characters");

        violation = violations.iterator().next();
        assertEquals("Part name cannot be longer than 120 characters", violation.getMessage());
    }

    @DisplayName("Description - Valid Input")
    @Test
    void testDescription_whenValidInputProvided_thenCorrect() {

        //given
        partDTO.setDescription("Brake rotor designed for optimal heat dissapation...");

        //when
        violations = validator.validate(partDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for description");
    }

    @DisplayName("Description - Invalid Input - Description Is Longer Than 255 Characters")
    @ParameterizedTest
    @CsvFileSource(resources = "/descriptionLongerThan255Characters.csv", numLinesToSkip = 1)
    void testDescription_whenDescritionIsLongerThan255Characters_thenValidationFailed(String descprion) {

        //given
        partDTO.setDescription(descprion);

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for description that is longer than 255 characters");

        violation = violations.iterator().next();
        assertEquals("Description cannot be longer than 255 characters", violation.getMessage());
    }

    @DisplayName("Prices - valid input")
    @Test
    void testPriceList_whenListIsPopulated_thenCorrect() {

        //given
        partDTO.setPrices(List.of(mock(Price.class), mock(Price.class)));

        //when
        violations = validator.validate(partDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for populated list");
    }

    @DisplayName("Prices - Invalid Input - List Is Null")
    @Test
    void testPriceList_whenListIsNull_thenValidationFailed() {

        //given
        partDTO.setPrices(null);

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for populated list");

        violation = violations.iterator().next();
        assertEquals("Price list cannot be null", violation.getMessage());
    }

    @DisplayName("Part Group - Valid Input")
    @Test
    void testPartGroup_whenValidInputProvided_thenCorrect() {

        //given
        partDTO.setPartGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build());

        //when
        violations = validator.validate(partDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for part group");
    }

    @DisplayName("Part Group - Invalid Input - Part Group Is Null")
    @Test
    void testPartGroup_whenPartGroupIsNull_thenValidationFailed() {

        //given
        partDTO.setPartGroup(null);

        //when
        violations = validator.validate(partDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for part group");

        violation = violations.iterator().next();
        assertEquals("Part group cannot be null", violation.getMessage());
    }

    @DisplayName("Vehicles - Valid Input")
    @Test
    void testVehicleList_whenListIsPopulated_thenCorrect() {

        //given
        partDTO.setVehicles(List.of(mock(Vehicle.class), mock(Vehicle.class)));

        //when
        violations = validator.validate(partDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for populated vehicle list");
    }

    @DisplayName("Vehicles - Invalid Input - Vehicle List Is Null")
    @Test
    void testVehicleList_whenVehicleListIsNull_thenValidationFailed() {

        partDTO.setVehicles(null);

        violations = validator.validate(partDTO);

        assertFalse(violations.isEmpty(), "Validation should fail for vehicle list that is null");

        violation = violations.iterator().next();
        assertEquals("Vehicle list cannot be null", violation.getMessage());
    }
}
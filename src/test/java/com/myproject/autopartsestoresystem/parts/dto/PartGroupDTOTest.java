package com.myproject.autopartsestoresystem.parts.dto;

import com.myproject.autopartsestoresystem.parts.entity.PartGroupType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PartGroupDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PartGroupDTO>> violations;
    private ConstraintViolation<PartGroupDTO> violation;
    private PartGroupDTO partGroupDTO;

    public PartGroupDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        partGroupDTO = PartGroupDTO.builder()
                .name(PartGroupType.FUEL_SYSTEM)
                .parts(new ArrayList<>())
                .build();
    }

    @DisplayName("Name - Valid Input")
    @Test
    void testNameValidation_whenValidInputProvided_thenCorrect() {

        //given
        partGroupDTO.setName(PartGroupType.ELECTRICAL_SYSTEM);

        //when
        violations = validator.validate(partGroupDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid name");
    }

    @Test
    void testNameValidation_whenInputIsNull_thenValidationFailed() {

        //given
        partGroupDTO.setName(null);

        //when
        violations = validator.validate(partGroupDTO);

        //then
        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Part group not selected");

    }

    @DisplayName("Parts list empty")
    @Test
    void testPartsList_whenListIsEmpty_thenCorrect() {

        //given
        partGroupDTO.setParts(new ArrayList<>());

        //when
        violations = validator.validate(partGroupDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for empty parts list");
    }

    @DisplayName("Parts List Is Null - Validation failed")
    @Test
    void testPartsList_whenListIsNull_thenValidationFailed() {

        //given
        partGroupDTO.setParts(null);

        //when
        violations = validator.validate(partGroupDTO);

        //then
        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Parts list cannot be null");
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        PartGroupDTO partGroupDTO2 = PartGroupDTO.builder()
                .name(PartGroupType.FUEL_SYSTEM)
                .parts(new ArrayList<>())
                .build();

        //when
        boolean isEqual = partGroupDTO2.equals(partGroupDTO);
        boolean isEqualHashCode = partGroupDTO2.hashCode() == partGroupDTO.hashCode();

        assertTrue(isEqual, "PartGroupDTO should be equal");
        assertTrue(isEqualHashCode, "PartGroupDTO hashCode should be equal");
    }
}
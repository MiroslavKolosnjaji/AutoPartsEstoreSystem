package com.myproject.autopartsestoresystem.vehicles.dto;

import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.models.entity.Model;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class VehicleDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<VehicleDTO>> violations;
    private ConstraintViolation<VehicleDTO> violation;
    private VehicleDTO vehicleDTO;


    public VehicleDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        Model model = Model.builder()
                .id(1)
                .name("318")
                .brand(Brand.builder().id(1).name("BMW").models(new HashSet<>()).build())
                .build();


        vehicleDTO = VehicleDTO.builder()
                .parts(new ArrayList<>())
                .model(model)
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();
    }

    @Test
    void testEngineType_whenValidInputProvided_thenCorrect() {

        //given
        vehicleDTO.setEngineType("2.0");

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid engine type");
    }

    @Test
    void testEngineType_whenEngineTypeIsEmpty_thenValidationFailed() {

        //given
        vehicleDTO.setEngineType("");

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty engine type");

        violation = violations.iterator().next();
        assertEquals("Engine type cannot be blank", violation.getMessage());
    }

    @Test
    void testEngineType_whenEngineTypeIsNull_thenValidationFailed() {

        //given
        vehicleDTO.setEngineType(null);

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for engine type that is NULL");

        violation = violations.iterator().next();
        assertEquals("Engine type cannot be blank", violation.getMessage());
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/engineTypeLongerThan120Characters.csv", numLinesToSkip = 1)
    void testEngineType_whenEngineTypeIsLongerThan120Characters_thenValidationFailed(String engineType) {

        //given
        vehicleDTO.setEngineType(engineType);

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for valid engine type");

        violation = violations.iterator().next();
        assertEquals("Engine type description cannot be longer than 120 characters", violation.getMessage());
    }

    @Test
    void testSeries_whenValidInputProvided_thenCorrect() {

        //given
        vehicleDTO.setSeries("Series 5");

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid series");
    }

    @Test
    void testSeries_whenSeriesIsEmpty_thenValidationFailed() {

        //given
        vehicleDTO.setSeries("");

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty series");

        violation = violations.iterator().next();
        assertEquals("Series cannot be blank", violation.getMessage());
    }

    @Test
    void testSeries_whenSeriesIsNull_thenValidationFailed() {

        //given
        vehicleDTO.setSeries(null);

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for series that is NULL");

        violation = violations.iterator().next();
        assertEquals("Series cannot be blank", violation.getMessage());
    }

    @Test
    void testModel_whenValidInputProvided_thenCorrect() {

        //given
        vehicleDTO.setModel(Model.builder().build());

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid model");
    }

    @Test
    void testModel_whenModelIsNull_thenValidationFailed() {

        //given
        vehicleDTO.setModel(null);

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for model that is NULL");

        violation = violations.iterator().next();
        assertEquals("Model cannot be null", violation.getMessage());
    }


    @Test
    void testParts_whenValidInputProvided_thenCorrect() {

        //given
        vehicleDTO.setParts(new ArrayList<>());

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid parts");
    }

    @Test
    void testParts_whenPartListIsNull_thenValidationFailed() {

        //given
        vehicleDTO.setParts(null);

        //when
        violations = validator.validate(vehicleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for part that is NULL");

        violation = violations.iterator().next();
        assertEquals("Part list cannot be null", violation.getMessage());
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        VehicleDTO vehicleDTO2 = VehicleDTO.builder()
                .parts(new ArrayList<>())
                .model(vehicleDTO.getModel())
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();

        //when
        boolean isEqual = vehicleDTO2.equals(vehicleDTO);
        boolean isEqualHashCode = vehicleDTO2.hashCode() == vehicleDTO.hashCode();

        assertTrue(isEqual, "VehicleDTO should be equal");
        assertTrue(isEqualHashCode, "VehicleDTO hashCode should be equal");
    }
}
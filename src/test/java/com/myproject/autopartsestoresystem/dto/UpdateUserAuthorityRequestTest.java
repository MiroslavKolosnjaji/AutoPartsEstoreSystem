package com.myproject.autopartsestoresystem.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class UpdateUserAuthorityRequestTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<UpdateUserAuthorityRequest>> violations;
    private ConstraintViolation<UpdateUserAuthorityRequest> violation;
    private UpdateUserAuthorityRequest updateUserAuthorityRequest;

    public UpdateUserAuthorityRequestTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        updateUserAuthorityRequest = new UpdateUserAuthorityRequest();
        updateUserAuthorityRequest.setAuthority("ADMIN");
        updateUserAuthorityRequest.setUpdateStatus("GRANT_AUTHORITY");
    }


    @Test
    void testAuthority_whenValidInputProvided_thenCorrect() {

        //given
        updateUserAuthorityRequest.setAuthority("ADMIN");

        //when
        violations = validator.validate(updateUserAuthorityRequest);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid authority input");
    }

    @Test
    void testAuthority_whenEmptyInputProvided_thenValidationFailed() {

        //given
        updateUserAuthorityRequest.setAuthority("");

        //when
        violations = validator.validate(updateUserAuthorityRequest);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty authority input");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Authority required");
    }

    @Test
    void testUpdateStatus_whenValidInputProvided_thenCorrect() {

        //given
        updateUserAuthorityRequest.setUpdateStatus("GRANT_AUTHORITY");

        //when
        violations = validator.validate(updateUserAuthorityRequest);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid update status input");
    }

    @Test
    void testUpdateStatus_whenEmptyInputProvided_thenValidationFailed(){

        //given
        updateUserAuthorityRequest.setUpdateStatus("");

        //when
        violations = validator.validate(updateUserAuthorityRequest);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty update status input");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Update status required");
    }
}
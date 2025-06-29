package com.myproject.autopartsestoresystem.users.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class UserDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<UserDTO>> violations;
    private ConstraintViolation<UserDTO> violation;
    private UserDTO userDTO;

    public UserDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }


    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();
    }


    @Test
    void testUsername_whenValidInputProvided_thenCorrect() {

        //given
        userDTO.setUsername("test@example.com");

        //when
        violations = validator.validate(userDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid username");
    }

    @Test
    void testUsername_whenInvalidFormatProvided_thenValidationFailed() {

        //given
        userDTO.setUsername("testexample.com");

        //when
        violations = validator.validate(userDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid username");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Username is not valid");
    }

    @Test
    void testUsername_whenEmptyInputProvided_thenValidationFailed() {

        //given
        userDTO.setUsername("");

        //when
        violations = validator.validate(userDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty username");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Username is required");
    }

    @Test
    void testPassword_whenValidInputProvided_thenCorrect() {

        //given
        userDTO.setPassword("1233456432");

        //when
        violations = validator.validate(userDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid password");
    }

    @Test
    void testPassword_whenInputWithLessThan8CharactersProvided_thenValidationFailed() {

        //given
        userDTO.setPassword("1234567");

        //when
        violations = validator.validate(userDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid password");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Your password must be at least 8 characters long");
    }

    @Test
    void testPassword_whenEmptyImputIsProvided_thenValidationFailed() {

        //given
        userDTO.setPassword("");

        //when
        violations = validator.validate(userDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty password");
    }

    @Test
    void testRoles_whenValidInputProvided_thenCorrect() {

        //given
        userDTO.setRoles(Set.of());

        //when
        violations = validator.validate(userDTO);


        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid roles");
    }

    @Test
    void testRoles_whenNullInputProvided_thenValidationFailed() {

        //given
        userDTO.setRoles(null);

        //when
        violations = validator.validate(userDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for null roles");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "User roles cannot be null");
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        UserDTO userDTO2 = UserDTO.builder()
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();

        boolean isEqual = userDTO.equals(userDTO2);
        boolean hashCodeEqual = userDTO.hashCode() == userDTO2.hashCode();

        assertEquals(userDTO, userDTO2);
        assertTrue(isEqual, "UserDTO should be equal to UserDTO2");
        assertTrue(hashCodeEqual, "UserDTO hashCode should be equal to UserDTO2 hashCode");
    }
}
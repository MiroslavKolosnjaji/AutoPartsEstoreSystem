package com.myproject.autopartsestoresystem.users.dto;

import com.myproject.autopartsestoresystem.users.entity.RoleName;
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
class RoleDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<RoleDTO>> violations;
    private ConstraintViolation<RoleDTO> violation;
    private RoleDTO roleDTO;

    public RoleDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        roleDTO = RoleDTO.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();
    }

    @Test
    void testName_whenValidInputProvided_thenCorrect() {

        //given
        roleDTO.setName(RoleName.ROLE_ADMIN);

        //when
        violations = validator.validate(roleDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid name");
    }

    @Test
    void testName_whenNullInputProvided_thenValidationFailed() {

        //given
        roleDTO.setName(null);

        //when
        violations = validator.validate(roleDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for null name");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(),"Role name is required");
    }


    @Test
    void isEquals_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

       RoleDTO roleDTO2 = RoleDTO.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();

        boolean isEqual = roleDTO.equals(roleDTO2);
        boolean hashCodeIsEqual = roleDTO.hashCode() == roleDTO2.hashCode();

        assertEquals(roleDTO, roleDTO2, "RoleDTO2 should be equal to RoleDTO1");
        assertTrue(isEqual, "RoleDTO2 should be equal to RoleDTO");
        assertTrue(hashCodeIsEqual, "RoleDTO2 hashCode should be equal to RoleDTO hashCode");
    }
}
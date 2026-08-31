package com.cws.shop.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cws.shop.dto.request.CreateUserRequestDto;
import com.cws.shop.dto.request.ResetPasswordRequestDto;

public class PasswordValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void tearDown() {
        factory.close();
    }

    @Test
    public void testCreateUserPasswordValid() {
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setMobileNumber("8123456789");
        dto.setPassword("Sssshinde@123");

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);

        // Should be valid (no violations)
        assertTrue(violations.isEmpty(), "Expected no validation violations but found: " + violations);
    }

    @Test
    public void testResetPasswordValid() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setNewPassword("Sssshinde@123");
        dto.setConfirmPassword("Sssshinde@123");

        Set<ConstraintViolation<ResetPasswordRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation violations but found: " + violations);
    }
}

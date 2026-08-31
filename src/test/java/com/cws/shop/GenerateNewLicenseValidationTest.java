package com.cws.shop;

import com.cws.shop.dto.request.GenerateNewLicenseRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerateNewLicenseValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void teardown() {
        factory.close();
    }

    private GenerateNewLicenseRequest baseRequest() {
        GenerateNewLicenseRequest req = new GenerateNewLicenseRequest();
        req.setProductId(1L);
        req.setLicenseType(com.cws.shop.model.LicPlanType.MONTHLY);
        req.setCustomerName("John Doe");
        req.setCustomerEmail("john@example.com");
        req.setActivationLimit(1);
        req.setActivationDate(LocalDate.now());
        req.setExpiryDate(LocalDate.now().plusDays(30));
        return req;
    }

    @Test
    public void invalidAlphabeticMobile_shouldFail() {
        GenerateNewLicenseRequest req = baseRequest();
        req.setMobileNumber("abcdefghij");

        Set<ConstraintViolation<GenerateNewLicenseRequest>> violations = validator.validate(req);

        // One or more violations expected; ensure at least one message indicates digit-only/length
        assertTrue(!violations.isEmpty());
        boolean matched = violations.stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains("digits only") || v.getMessage().toLowerCase().contains("must be exactly"));
        assertTrue(matched);
    }

    @Test
    public void invalidMixedMobile_shouldFail() {
        GenerateNewLicenseRequest req = baseRequest();
        req.setMobileNumber("abc1234567");

        Set<ConstraintViolation<GenerateNewLicenseRequest>> violations = validator.validate(req);
        assertTrue(!violations.isEmpty());
        boolean matched = violations.stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains("digits only") || v.getMessage().toLowerCase().contains("must be exactly"));
        assertTrue(matched);
    }

    @Test
    public void invalidSpecialCharsMobile_shouldFail() {
        GenerateNewLicenseRequest req = baseRequest();
        req.setMobileNumber("@#$%^&*()");

        Set<ConstraintViolation<GenerateNewLicenseRequest>> violations = validator.validate(req);
        assertTrue(!violations.isEmpty());
        boolean matched = violations.stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains("digits only") || v.getMessage().toLowerCase().contains("must be exactly"));
        assertTrue(matched);
    }

    @Test
    public void invalidTooShortMobile_shouldFail() {
        GenerateNewLicenseRequest req = baseRequest();
        req.setMobileNumber("98765");

        Set<ConstraintViolation<GenerateNewLicenseRequest>> violations = validator.validate(req);
        assertTrue(!violations.isEmpty());
        boolean matched = violations.stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains("exactly 10 digits") || v.getMessage().toLowerCase().contains("size"));
        assertTrue(matched);
    }

    @Test
    public void invalidTooLongMobile_shouldFail() {
        GenerateNewLicenseRequest req = baseRequest();
        req.setMobileNumber("98765432101234");

        Set<ConstraintViolation<GenerateNewLicenseRequest>> violations = validator.validate(req);
        assertTrue(!violations.isEmpty());
        boolean matched = violations.stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains("exactly 10 digits") || v.getMessage().toLowerCase().contains("size"));
        assertTrue(matched);
    }

    @Test
    public void validMobile_shouldPass() {
        GenerateNewLicenseRequest req = baseRequest();
        req.setMobileNumber("9876543210");

        Set<ConstraintViolation<GenerateNewLicenseRequest>> violations = validator.validate(req);

        assertTrue(violations.isEmpty());
    }
}

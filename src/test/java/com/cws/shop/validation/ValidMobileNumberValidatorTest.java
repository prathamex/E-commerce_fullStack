package com.cws.shop.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidMobileNumberValidatorTest {

    private static Validator validator;

    static class TestBean {
        @ValidMobileNumber
        private String mobile;

        TestBean(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }
    }

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        // nothing to clean up
    }

    @Test
    void whenContainsLetters_thenErrorMessageIndicatesLettersNotPermitted() {
        Set<ConstraintViolation<TestBean>> violations = validator.validate(new TestBean("12345abcde"));
        assertFalse(violations.isEmpty(), "Expected a validation error when letters are present");
        String msg = violations.iterator().next().getMessage();
        assertTrue(msg.contains("contain only digits"), 
                "Message should indicate letters are not permitted. Actual: " + msg);
    }

    @Test
    void whenContainsSymbols_thenErrorMessageIndicatesOnlyDigits() {
        Set<ConstraintViolation<TestBean>> violations = validator.validate(new TestBean("12345-6789"));
        assertFalse(violations.isEmpty(), "Expected a validation error when symbols are present");
        String msg = violations.iterator().next().getMessage();
        assertTrue(msg.contains("contain only digits"),
                "Message should indicate non-numeric characters are not allowed. Actual: " + msg);
    }

    @Test
    void whenTooShort_thenErrorMessageIndicatesLength() {
        Set<ConstraintViolation<TestBean>> violations = validator.validate(new TestBean("12345"));
        assertFalse(violations.isEmpty(), "Expected a validation error when number is too short");
        String msg = violations.iterator().next().getMessage();
        assertTrue(msg.contains("10 digits"),
                "Message should indicate required length. Actual: " + msg);
    }

    @Test
    void whenValid_thenNoViolations() {
        Set<ConstraintViolation<TestBean>> violations = validator.validate(new TestBean("9876543210"));
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid 10-digit number");
    }
}

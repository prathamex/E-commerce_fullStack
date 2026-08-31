package com.cws.shop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidMobileNumberValidator implements ConstraintValidator<ValidMobileNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Let other annotations (e.g., @NotBlank) handle null/blank values
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        String trimmed = value.trim();

        // If contains any non-digit characters (letters, special characters, etc.)
        if (!trimmed.matches("^\\d+$")) {
            // Provide a clear, specific message for non-digit characters so clients
            // can show that letters/symbols are not permitted (was reported as a bug)
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Mobile number must contain only digits (0-9). Letters and special characters are not allowed.")
                   .addConstraintViolation();
            return false;
        }

        // Check length - only executed if the phone number contains only digits
        if (trimmed.length() != 10) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Mobile number must be exactly 10 digits long.")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}

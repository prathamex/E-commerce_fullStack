package com.cws.shop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidMobileNumberValidator.class)
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface ValidMobileNumber {
    // The validator provides specific messages for different validation scenarios
    String message() default "Invalid phone number format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

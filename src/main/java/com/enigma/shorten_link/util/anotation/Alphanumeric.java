package com.enigma.shorten_link.util.anotation;


import com.enigma.shorten_link.util.validator.ShortUrlConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented

@Constraint(validatedBy = ShortUrlConstraintValidator.class)

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)


public @interface Alphanumeric {
    String message() default "Only accept alphanumeric character";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

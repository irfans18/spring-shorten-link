package com.enigma.shorten_link.util.validator;

import com.enigma.shorten_link.util.anotation.Alphanumeric;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ShortUrlConstraintValidator implements ConstraintValidator<Alphanumeric, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // accept only alphanumeric character
        String pattern ="^[a-zA-Z0-9_]*$";
        return value.matches(pattern);
    }


}

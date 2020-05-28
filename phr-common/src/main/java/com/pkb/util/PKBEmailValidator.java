package com.pkb.util;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

/*
 * org.apache.commons.validator.EmailValidator rejects some valid emails.
 * It is wrapped in order to give us flexibility to either replace it or guard some of the corner cases (if the need comes).
 */

/**
 * Validated Email addresses.
 */
@Component
public class PKBEmailValidator {
    private static final EmailValidator VALIDATOR = EmailValidator.getInstance();

    public boolean isValidEmail(String email) {
        return VALIDATOR.isValid(email);
    }

    public boolean isNotValid(String email) {
        return !isValidEmail(email);
    }
}

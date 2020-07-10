package com.pkb.common.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.ApplicationException;

import com.google.common.collect.Lists;

@ApplicationException(rollback = true)
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<Error> errors = new ArrayList<>();

    public ValidationException(String errorCode, String description, Object... arguments) {
        this(ErrorBuilder.errorOf(errorCode, description, arguments));
    }

    public ValidationException(Error... error) {
        this(Lists.newArrayList(error));
    }

    public ValidationException(List<Error> errors) {
        super();
        this.errors.addAll(errors);
    }

    public List<Error> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}

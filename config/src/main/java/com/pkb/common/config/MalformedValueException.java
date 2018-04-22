package com.pkb.common.config;

import static java.lang.String.format;

public class MalformedValueException extends ConfigurationException {

    public MalformedValueException(String key, Class<?> expectedType, String actualValue) {
        super(format("malformed value for configuration key [%s]: %s (expected type: %s)", key, actualValue, expectedType.getSimpleName()));
    }

}

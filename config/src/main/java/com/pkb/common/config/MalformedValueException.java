package com.pkb.common.config;

import static java.lang.String.format;

public class MalformedValueException extends ConfigurationException {

    private static final long serialVersionUID = -7231163479288724686L;

    public MalformedValueException(String key, Class<?> expectedType, String actualValue) {
        super(format("malformed value=[%s] for configuration key=[%s] (expected type=[%s])", actualValue, key, expectedType.getSimpleName()));
    }

}

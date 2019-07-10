package com.pkb.common.config;

import static java.lang.String.format;

public class MissingValueException extends ConfigurationException {

    public MissingValueException(String key) {
        super(format("missing configuration key: [%s]", key));
    }

}

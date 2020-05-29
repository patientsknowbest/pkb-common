package com.pkb.common.config;

import static java.lang.String.format;

public class MissingValueException extends ConfigurationException {

    private static final long serialVersionUID = -1772433778851166147L;

    public MissingValueException(String key) {
        super(format("missing configuration key: [%s]", key));
    }

}

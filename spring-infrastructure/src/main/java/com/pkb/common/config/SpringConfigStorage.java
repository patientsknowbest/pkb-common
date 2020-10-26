package com.pkb.common.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

public class SpringConfigStorage extends AbstractBaseConfigStorage implements ImmutableConfigStorage {


    private final Environment environment;

    public SpringConfigStorage(@NotNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getString(String key) {
        return environment.getProperty(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }


}

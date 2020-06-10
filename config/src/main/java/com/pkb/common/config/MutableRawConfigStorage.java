package com.pkb.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import io.vavr.control.Either;

public final class MutableRawConfigStorage extends AbstractBaseConfigStorage {

    private final Map<String, String> overrideMap = new HashMap<>();
    private final ImmutableRawConfigStorage configStorage;

    MutableRawConfigStorage(ImmutableRawConfigStorage configStorage) {
        this.configStorage = configStorage;
    }

    @Override
    public String getString(String key) {
        return getOverriddenOrOriginalValue(key, () -> configStorage.getString(key));
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getOverriddenOrOriginalValue(key, () -> configStorage.getString(key, defaultValue));
    }

    @Override
    public boolean isMutableConfigEnabled() {
        return true;
    }

    @Override
    public void setValue(String key, String value) {
        if (!MUTABLE_CONFIG_KEY.equals(key)) {
            overrideMap.put(key, value);
        }
    }

    @Override
    public void reset() {
        overrideMap.clear();
    }

    @Override
    public ConfigStorage getImmutableConfig() {
        return configStorage;
    }

    private String getOverriddenOrOriginalValue(String key, Supplier<String> originalSupplier) {
        String overriddenValue = overrideMap.get(key);
        if (overriddenValue != null) {
            return overriddenValue;
        }
        return originalSupplier.get();
    }

    @Override
    protected <P> Either<ConfigurationException, P> readValue(String key, Class<P> expectedType, Parser<P> parser) {
        String overriddenValue = overrideMap.get(key);
        if (overriddenValue != null) {
            return parseValue(key, overriddenValue, expectedType, parser).orElse(() -> parseValue(key, configStorage.getString(key), expectedType, parser));
        }
        return parseValue(key, configStorage.getString(key), expectedType, parser);
    }
}

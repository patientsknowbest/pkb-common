package com.pkb.common.config;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

import io.vavr.control.Either;

public final class ImmutableRawConfigStorage extends AbstractBaseConfigStorage {

    public static final ImmutableRawConfigStorage EMPTY = new ImmutableRawConfigStorage(emptyMap());

    static ImmutableRawConfigStorage createDefault() {
        return LayeredLoader.defaultLoader().load();
    }

    public static ImmutableRawConfigStorage merge(ImmutableRawConfigStorage original, ImmutableRawConfigStorage overrides) {
        Map<String, String> mergedStorage = new HashMap<>(original.storage);
        mergedStorage.putAll(overrides.storage);
        return new ImmutableRawConfigStorage(unmodifiableMap(mergedStorage));
    }

    private final Map<String, String> storage;

    ImmutableRawConfigStorage(Map<String, String> storage) {
        this.storage = storage;
    }

    @Override
    protected <P> Either<ConfigurationException, P> readValue(String key, Class<P> expectedType, Parser<P> parser) {
        return parseValue(key, storage.get(key), expectedType, parser);
    }

    @Override
    public String getString(String key) {
        return storage.get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return storage.getOrDefault(key, defaultValue);
    }

    @Override
    public boolean isMutableConfigEnabled() {
        return getBoolean(MUTABLE_CONFIG_KEY, false);
    }

    @Override
    public void setValue(String key, String value) {
        /* no-op */
    }

    @Override
    public OverrideRemovalResult removeOverrideAtKey(String key) {
        return OverrideRemovalResult.NO_OP_AS_CONFIG_IS_IMMUTABLE;
    }

    @Override
    public void reset() {
        /* no-op */
    }

    @Override
    public ConfigStorage getImmutableConfig() {
        return this;
    }

    @Override
    public boolean isFakeDateTimeServiceEnabled() {
        return getBoolean("fakedatetimeservice.enabled", false);
    }
}

package com.pkb.common.config;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

public final class ImmutableRawConfigStorage extends AbstractBaseConfigStorage {

    public static final ImmutableRawConfigStorage EMPTY = new ImmutableRawConfigStorage(emptyMap());

    static ImmutableRawConfigStorage createDefault() {
        return LayeredLoader.defaultLoader().load();
    }

    static ImmutableRawConfigStorage merge(ImmutableRawConfigStorage original, ImmutableRawConfigStorage overrides) {
        Map<String, String> mergedStorage = new HashMap<>(original.storage);
        mergedStorage.putAll(overrides.storage);
        return new ImmutableRawConfigStorage(unmodifiableMap(mergedStorage));
    }

    private final Map<String, String> storage;

    ImmutableRawConfigStorage(Map<String, String> storage) {
        this.storage = storage;
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
    public void setValue(String key, String value) {
        //No-op
    }

    @Override
    public OverrideRemovalResult removeOverrideAtKey(String key) {
        return OverrideRemovalResult.NO_OP_AS_CONFIG_IS_IMMUTABLE;
    }

    @Override
    public void reset() {
        //No-op
    }

}

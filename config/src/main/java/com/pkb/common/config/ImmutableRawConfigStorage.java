package com.pkb.common.config;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public final class ImmutableRawConfigStorage extends AbstractBaseConfigStorage implements ImmutableConfigStorage {

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

}

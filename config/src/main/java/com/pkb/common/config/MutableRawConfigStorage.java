package com.pkb.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class MutableRawConfigStorage extends AbstractBaseConfigStorage implements MutableConfigStorage {

    private final Map<String, String> overrideMap = new HashMap<>();
    private final ImmutableConfigStorage configStorage;

    MutableRawConfigStorage(ImmutableConfigStorage configStorage) {
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
    public void setValue(String key, String value) {
        if (!MUTABLE_CONFIG_KEY.equals(key)) {
            overrideMap.put(key, value);
        }
    }

    @Override
    public OverrideRemovalResult removeOverrideAtKey(String key) {
        if (overrideMap.containsKey(key)) {
            overrideMap.remove(key);
            return OverrideRemovalResult.REMOVED;
        } else {
            return OverrideRemovalResult.KEY_NOT_FOUND;
        }
    }

    @Override
    public void reset() {
        overrideMap.clear();
    }

    @Override
    public ImmutableConfigStorage getImmutableConfig() {
        return configStorage;
    }

    private String getOverriddenOrOriginalValue(String key, Supplier<String> originalSupplier) {
        if (overrideMap.containsKey(key)) {
            return overrideMap.get(key);
        }
        return originalSupplier.get();
    }
}

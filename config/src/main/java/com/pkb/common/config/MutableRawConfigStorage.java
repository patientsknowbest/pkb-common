package com.pkb.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class MutableRawConfigStorage extends AbstractMutableConfigStorage {

    private final Map<String, String> overrideMap = new HashMap<>();
    private final ConfigStorage configStorage;

    MutableRawConfigStorage(ConfigStorage configStorage) {
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
    void setValueInternal(String key, String value) {
        overrideMap.put(key, value);
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

    private String getOverriddenOrOriginalValue(String key, Supplier<String> originalSupplier) {
        if (overrideMap.containsKey(key)) {
            return overrideMap.get(key);
        }
        return originalSupplier.get();
    }

    @Override
    public boolean isMutableConfigEnabled() {
        return true;
    }
}

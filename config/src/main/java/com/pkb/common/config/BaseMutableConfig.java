package com.pkb.common.config;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseMutableConfig implements BaseConfig {

    Map<String, String> configMap;

    /* HashMap as it allows null value for a key */
    BaseMutableConfig() {
        this.configMap = new HashMap<>();
    }

    abstract BaseConfig getDefaultConfig();

    @Override
    public void setValue(String key, String value) {
        configMap.put(key, value);
    }

    @Override
    public void reset() {
        configMap.clear();
    }

    @Override
    public boolean isMutableConfigEnabled() {
        return true;
    }

    Optional<Integer> getIntValue(String intStr) {
        try {
            return Optional.of(Integer.parseInt(intStr));
        } catch (NumberFormatException ignored) {}
        return Optional.empty();
    }

    Optional<Long> getLongValue(String longStr) {
        try {
            return Optional.of(Long.parseLong(longStr));
        } catch (NumberFormatException ignored) {}
        return Optional.empty();
    }

    Optional<Boolean> getBooleanValue(String boolStr) {
        if (boolStr == null || (!boolStr.toLowerCase().equals("true") && !boolStr.toLowerCase().equals("false"))) {
            return Optional.empty();
        }
        return Optional.of(Boolean.parseBoolean(boolStr));
    }

    Optional<Path> getPathValue(String pathStr) {
        try {
            if (pathStr != null) {
                return Optional.of(Paths.get(pathStr));
            }
        } catch (InvalidPathException ignored) {}
        return Optional.empty();
    }

    @Override
    public String getBaseURL() {
        String baseUrl = configMap.get("baseUrl");
        if (baseUrl != null) {
            return baseUrl;
        }
        return getDefaultConfig().getBaseURL();
    }

    @Override
    public boolean isFakeDateTimeServiceEnabled() {
        return getBooleanValue(configMap.get("fakeDataTimeServiceEnabled"))
                .orElseGet(() -> getDefaultConfig().isFakeDateTimeServiceEnabled());
    }
}

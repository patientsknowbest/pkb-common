package com.pkb.common.config;

public interface ConfigStorage {

    Boolean getBoolean(String key);

    Boolean getBoolean(String key, Boolean defaultValue);

    String getString(String key);

    String getString(String key, String defaultValue);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    long getLong(String key);

    long getLong(String key, long defaultValue);

    boolean isMutableConfigEnabled();

    void setValue(String key, String value);

    void reset();

    ConfigStorage getImmutableConfig();
}

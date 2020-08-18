package com.pkb.common.config;

public interface ConfigStorage {

    Boolean getBoolean(String key);

    Boolean getBoolean(String key, Boolean defaultValue);

    String getString(String key);

    String getString(String key, String defaultValue);

    Integer getInt(String key);

    Integer getInt(String key, int defaultValue);

    Long getLong(String key);

    Long getLong(String key, long defaultValue);

    boolean isMutableConfigEnabled();

    void setValue(String key, String value);

    OverrideRemovalResult removeOverrideAtKey(String key);

    void reset();

    ConfigStorage getImmutableConfig();
}

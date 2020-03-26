package com.pkb.common.config;

public interface BaseConfig {
    String getBaseURL();
    boolean isMutableConfigEnabled();
    boolean isFakeDateTimeServiceEnabled();
    void setValue(String key, String value);
    void reset();
}

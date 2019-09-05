package com.pkb.common;

public interface MutableConfig {
    void setConfig(String key, String value);
    String getConfig(String key);
}

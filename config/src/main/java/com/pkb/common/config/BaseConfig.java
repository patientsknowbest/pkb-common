package com.pkb.common.config;

public interface BaseConfig {
    String getBaseURL();
    boolean isFakeDateTimeServiceEnabled();
    String getPulsarServiceURL();
    String getPulsarDefaultNamespce();
    boolean isPulsarServiceRegistrationEnabled();
    boolean isPulsarTestSupportServicesEnabled();
}

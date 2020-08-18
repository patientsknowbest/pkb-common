package com.pkb.common.config;

public interface PkbConfig extends BaseConfig {

    boolean isConversationAssignEnabled();
    boolean isConversationArchiveEnabled();
    String getSynertecApiClientId();
    int getSlowDocRefQueryAlertThresholdSeconds();
}

package com.pkb.common.config;

public interface PkbConfig extends BaseConfig {

    default boolean isConversationAssignEnabled() {
        return getConfigStorage().getBoolean("feature.conversationAssignEnabled", false);
    }

    default boolean isConversationArchiveEnabled() {
        return getConfigStorage().getBoolean("feature.conversationArchiveEnabled", false);
    }
}

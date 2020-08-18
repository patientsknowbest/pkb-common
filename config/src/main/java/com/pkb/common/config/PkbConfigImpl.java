package com.pkb.common.config;

public class PkbConfigImpl extends AbstractBaseConfig implements PkbConfig {

    public PkbConfigImpl(ConfigStorage storage) {
        super(storage);
    }

    @Override
    public boolean isConversationAssignEnabled() {
        return storage.getBoolean("feature.conversationAssignEnabled", false);
    }

    @Override
    public boolean isConversationArchiveEnabled() {
        return storage.getBoolean("feature.conversationArchiveEnabled", false);
    }

    @Override
    public String getSynertecApiClientId() {
        return storage.getString("synertecApiClientId", "synertec-user-test");
    }

    @Override
    public int getSlowDocRefQueryAlertThresholdSeconds() {
        return storage.getInt("slowDocRefQueryAlertThresholdSeconds", 30);
    }
}

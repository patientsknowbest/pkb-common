package com.pkb.common.config;

import java.util.concurrent.TimeUnit;

public interface PkbConfigInterface extends BaseConfig {

    default boolean isConversationAssignEnabled() {
        return getConfigStorage().getBoolean("feature.conversationAssignEnabled", false);
    }

    default boolean isConversationArchiveEnabled() {
        return getConfigStorage().getBoolean("feature.conversationArchiveEnabled", false);
    }

    default int getLetterInvitationTokenSize() {
        return getConfigStorage().getInt("letter.invitation.token.size", 10);
    }

    default int getLetterInvitationAccessCodeSize() {
        return getConfigStorage().getInt("letter.invitation.access.code.size", 10);
    }

    default int getLetterInvitationExpiry() {
        return getConfigStorage().getInt("letter.invitation.token.expiry", (int) TimeUnit.DAYS.toSeconds(70L));
    }

    default boolean isDocumentDplEncryptionEnabled() {
        return getConfigStorage().getBoolean("feature.documentDplEncryptionEnabled", false);
    }
}

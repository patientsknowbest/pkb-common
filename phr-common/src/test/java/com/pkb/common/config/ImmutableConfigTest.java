package com.pkb.common.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ImmutableConfigTest {

    private final PhrConfig underTestPhrSpringConfig = new ImmutablePhrConfig(RawConfigStorage.createDefault());
    private final FhirConfig underTestFhirSpringConfig = new ImmutableFhirConfig(RawConfigStorage.createDefault());

    @Test
    public void testLetterConfigurationUsesDefaults() throws Exception {
        assertEquals(LetterInvitationSettings.DEFAULT_ACCESS_CODE_SIZE, underTestPhrSpringConfig.getLetterInvitationAccessCodeSize());
        assertEquals(LetterInvitationSettings.DEFAULT_TOKEN_SIZE, underTestPhrSpringConfig.getLetterInvitationTokenSize());
        assertEquals(LetterInvitationSettings.DEFAULT_EXPIRY_IN_SECONDS, underTestPhrSpringConfig.getLetterInvitationExpiry());
    }

    @Test
    public void testIsFhirExperimental_isTrueByDefault() {
        assertFalse(underTestFhirSpringConfig.isFhirApiExperimental());
    }
}

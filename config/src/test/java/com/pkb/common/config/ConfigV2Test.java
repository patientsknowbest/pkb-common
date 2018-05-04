package com.pkb.common.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigV2Test {

    private final ConfigV2 underTest = ConfigV2.getInstance();

    @Test
    public void testLetterConfigurationUsesDefaults() throws Exception {
        assertEquals(LetterInvitationSettings.DEFAULT_ACCESS_CODE_SIZE, underTest.getLetterInvitationAccessCodeSize());
        assertEquals(LetterInvitationSettings.DEFAULT_TOKEN_SIZE, underTest.getLetterInvitationTokenSize());
        assertEquals(LetterInvitationSettings.DEFAULT_EXPIRY_IN_SECONDS, underTest.getLetterInvitationExpiry());
    }

    @Test
    public void testGetRestApiClientId_defaultValueShouldBeNull() {
        assertNull(underTest.getRestApiClientId());
    }

    @Test
    public void testIsFhirExperimental_isTrueByDefault() {
        assertTrue(underTest.isFhirApiExperimental());
    }
}

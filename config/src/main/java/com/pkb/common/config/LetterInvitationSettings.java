package com.pkb.common.config;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class LetterInvitationSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_TOKEN_SIZE = 10;

    public static final int DEFAULT_ACCESS_CODE_SIZE = 10;

    public static final int DEFAULT_EXPIRY_IN_SECONDS = (int) TimeUnit.DAYS.toSeconds(70L);

    private int tokenSize;

    private int tokenExpiryInSeconds;

    private int accessCodeSize;

    public LetterInvitationSettings() {
        this(DEFAULT_TOKEN_SIZE, DEFAULT_EXPIRY_IN_SECONDS, DEFAULT_ACCESS_CODE_SIZE);
    }

    public LetterInvitationSettings(int tokenSize, int tokenExpiryInSeconds, int accessCodeSize) {
        if (tokenSize < DEFAULT_TOKEN_SIZE) {
            throw new IllegalArgumentException("Token size must be greater than " + DEFAULT_TOKEN_SIZE);
        }
        if (accessCodeSize < DEFAULT_ACCESS_CODE_SIZE) {
            throw new IllegalArgumentException("Access code size must be greater than " + DEFAULT_ACCESS_CODE_SIZE);
        }
        if (tokenExpiryInSeconds < 1) {
            throw new IllegalArgumentException("Token expiry must be greater than 0");
        }

        this.tokenSize = tokenSize;
        this.tokenExpiryInSeconds = tokenExpiryInSeconds;
        this.accessCodeSize = accessCodeSize;
    }

    public int getTokenSize() {
        return tokenSize;
    }

    public int getTokenExpiryInSeconds() {
        return tokenExpiryInSeconds;
    }

    public int getAccessCodeSize() {
        return accessCodeSize;
    }

}

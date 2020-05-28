package com.pkb.util;

import java.time.Instant;
import java.time.ZoneId;

public class Constants {

    public static final ZoneId APPLICATION_TZ = ZoneId.of("Europe/London");
    public static final String NHS_LOGIN_JTI = "nhs.login.jti";
    public static final String NHS_LOGIN_CORRELATION_ID = "nhs.login.correlation.id";
    public static final String NHS_LOGIN_REDIRECTION_PATH = "nhs.login.redirection.path";
    private static final long POSTGRES_DATE_NEGATIVE_INFINITY_LONG = -9223372036832400000L;
    public static final Instant POSTGRES_DATE_NEGATIVE_INFINITY_INSTANT = Instant.ofEpochMilli(POSTGRES_DATE_NEGATIVE_INFINITY_LONG);

    private Constants() {
    }

    public static boolean isNegativeInfinity(Instant instant) {
        return POSTGRES_DATE_NEGATIVE_INFINITY_INSTANT.equals(instant);
    }
}

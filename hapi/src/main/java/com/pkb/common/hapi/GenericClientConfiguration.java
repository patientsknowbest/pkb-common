package com.pkb.common.hapi;

import java.time.Duration;

public interface GenericClientConfiguration {

    default Duration getConnectTimeout() {
        return Duration.ofSeconds(10);
    }

    default Duration getReadTimeout() {
        return Duration.ofSeconds(10);
    }

    default Duration getWriteTimeout() {
        return Duration.ofSeconds(10);
    }

    default boolean getRetryOnConnectionFailure() {
        return false;
    }

    default int getMaxIdleConnections() {
        return 256;
    }
}

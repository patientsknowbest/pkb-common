package com.pkb.common.datetime;

import java.io.Serializable;
import java.time.Clock;
import java.time.temporal.TemporalUnit;

public class DefaultDateTimeService implements DateTimeService, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void setFixedCurrentTimeForTesting(String ignored) {
        throw new IllegalStateException("Not currently in a test environment");
    }

    @Override
    public void moveTime(long amountToAdd, TemporalUnit unit) {
        throw new IllegalStateException("Not currently in a test environment");
    }

    @Override
    public void forgetFixedCurrentTimeForTesting() {
        throw new IllegalStateException("Not currently in a test environment");
    }
}

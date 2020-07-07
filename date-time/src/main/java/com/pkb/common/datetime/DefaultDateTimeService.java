package com.pkb.common.datetime;

import java.time.Clock;
import java.time.temporal.TemporalUnit;

public class DefaultDateTimeService implements DateTimeService {

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
    public long nowNanoTime() {
        return System.nanoTime();
    }

    @Override
    public void forgetFixedCurrentTimeForTesting() {
        throw new IllegalStateException("Not currently in a test environment");
    }
}

package com.pkb.common.datetime;

import java.time.Clock;

public class DefaultDateTimeService implements DateTimeService {

    @Override
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void setFixedCurrentTimeForTesting(String input) {
        throw new IllegalStateException("Not currently in a test environment");
    }

    @Override
    public void forgetFixedCurrentTimeForTesting() {
        throw new IllegalStateException("Not currently in a test environment");
    }
}

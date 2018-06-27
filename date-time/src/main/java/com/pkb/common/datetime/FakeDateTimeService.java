package com.pkb.common.datetime;

import java.time.Clock;
import java.time.ZonedDateTime;

public class FakeDateTimeService implements DateTimeService {

    private volatile Clock currentFixedClock;

    @Override public Clock clock() {
        if (currentFixedClock == null) {
            throw new IllegalStateException("current datetime is not set");
        }
        return currentFixedClock;
    }

    public void setCurrentTime(String input) {
        ZonedDateTime zdt = ZonedDateTime.parse(input);
        currentFixedClock = Clock.fixed(zdt.toInstant(), zdt.getZone());
    }

}

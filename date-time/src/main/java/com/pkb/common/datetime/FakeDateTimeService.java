package com.pkb.common.datetime;

import java.time.Clock;
import java.time.ZonedDateTime;

public class FakeDateTimeService implements DateTimeService {

    private volatile Clock currentFixedClock;

    private final DateTimeService fallbackService;

    public FakeDateTimeService(DateTimeService fallbackService) {
        this.fallbackService = fallbackService;
    }

    @Override public Clock clock() {
        if (currentFixedClock == null) {
            return fallbackService.clock();
        }
        return currentFixedClock;
    }

    public void setCurrentTime(String input) {
        ZonedDateTime zdt = ZonedDateTime.parse(input);
        currentFixedClock = Clock.fixed(zdt.toInstant(), zdt.getZone());
    }

    public void forgetCurrentTime() {
        this.currentFixedClock = null;
    }

}

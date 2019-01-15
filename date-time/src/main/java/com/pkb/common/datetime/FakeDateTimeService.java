package com.pkb.common.datetime;

import java.lang.invoke.MethodHandles;
import java.time.Clock;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeDateTimeService implements DateTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private volatile Clock currentFixedClock;

    private final DateTimeService fallbackService;

    public FakeDateTimeService(DateTimeService fallbackService) {
        this.fallbackService = fallbackService;
    }

    @Override
    public Clock clock() {
        if (currentFixedClock == null) {
            return fallbackService.clock();
        }
        return currentFixedClock;
    }

    @Override
    public void setFixedCurrentTimeForTesting(String input) {
        ZonedDateTime zdt = ZonedDateTime.parse(input);
        currentFixedClock = Clock.fixed(zdt.toInstant(), zdt.getZone());
        LOGGER.info("Set fixed fake date time to: {}", currentFixedClock);
    }

    @Override
    public void forgetFixedCurrentTimeForTesting() {
        this.currentFixedClock = null;
        LOGGER.info("Cleared fixed fake date time.");
    }

}

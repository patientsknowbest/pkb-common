package com.pkb.common.datetime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

public class FakeDateTimeService implements DateTimeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private volatile ZonedDateTime currentFixedTime;
    volatile Clock currentFixedClock;

    private final DateTimeService fallbackService;

    public FakeDateTimeService() {
        this(new DefaultDateTimeService());
    }

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
    public void setFixedCurrentTimeForTesting(String isoZonedDateTime) {
        fixTime(ZonedDateTime.parse(isoZonedDateTime));
    }

    @Override
    public void moveTime(long amountToAdd, TemporalUnit unit) {
        fixTime(currentFixedTime.plus(amountToAdd, unit));
    }

    @Override
    public long nowNanoTime() {
        Instant now = now();
        return TimeUnit.SECONDS.toNanos(now.getEpochSecond()) + now.getNano();
    }

    @Override
    public void forgetFixedCurrentTimeForTesting() {
        this.currentFixedClock = null;
        LOGGER.info("Cleared fixed fake date time.");
    }

    private void fixTime(ZonedDateTime zdt) {
        currentFixedTime = zdt;
        currentFixedClock = Clock.fixed(zdt.toInstant(), zdt.getZone());
        LOGGER.info("Set fixed fake date time to: {}", currentFixedClock);
    }
}

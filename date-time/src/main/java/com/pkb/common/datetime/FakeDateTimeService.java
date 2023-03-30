package com.pkb.common.datetime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class FakeDateTimeService implements DateTimeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    volatile ZonedDateTime currentFixedTime;

    private final DateTimeService fallbackService;

    public FakeDateTimeService() {
        this(new DefaultDateTimeService());
    }

    public FakeDateTimeService(DateTimeService fallbackService) {
        this.fallbackService = fallbackService;
    }

    @Override
    public Clock clock() {
        return new ClockShim(getCurrentZoneId());
    }

    ZoneId getCurrentZoneId() {
        return Optional.ofNullable(currentFixedTime).map(ZonedDateTime::getZone).orElseGet(fallbackService.clock()::getZone);
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
    public void moveTime(TemporalAmount duration) {
        fixTime(currentFixedTime.plus(duration));
    }

    @Override
    public long nowNanoTime() {
        Instant now = now();
        return TimeUnit.SECONDS.toNanos(now.getEpochSecond()) + now.getNano();
    }

    @Override
    public void forgetFixedCurrentTimeForTesting() {
        this.currentFixedTime = null;
        LOGGER.info("Cleared fixed fake date time.");
    }

    private void fixTime(ZonedDateTime zdt) {
        currentFixedTime = zdt;
        LOGGER.info("Set fixed fake date time to: {}", currentFixedTime);
    }


    class ClockShim extends Clock {

        private final ZoneId zone;

        ClockShim(ZoneId zone) {
            this.zone = zone;
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return new ClockShim(zone);
        }

        @Override
        public Instant instant() {
            return Optional.ofNullable(currentFixedTime).map(ZonedDateTime::toInstant).orElseGet(fallbackService.clock()::instant);
        }
    }
}

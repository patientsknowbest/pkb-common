package com.pkb.common.datetime;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;

/**
 * Override of FakeDateTimeService that supports automatically advancing
 * time by a fixed amount each time the services is asked for a time.
 * DO NOT USE in E2E under any circumstances, it will cause extreme
 * flakiness. This is intended for use in  unit and integration tests only
 * for classes/subsystems that use temporal ordering.
 */
public class AutoAdvanceFakeDateTimeService extends FakeDateTimeService {

    private TemporalAmount autoAdvanceDuration = null;

    @Override
    public Clock clock() {
        return new AutoAdvanceClockShim(getCurrentZoneId());
    }

    private class AutoAdvanceClockShim extends ClockShim {

        AutoAdvanceClockShim(ZoneId zone) {
            super(zone);
        }

        @Override
        public Instant instant() {
            if (currentFixedTime != null && autoAdvanceDuration != null) {
                moveTime(autoAdvanceDuration);
            }
            return super.instant();
        }
    }

    public void setAutoAdvanceDuration(TemporalAmount autoAdvanceDuration) {
        this.autoAdvanceDuration = autoAdvanceDuration;
    }
}

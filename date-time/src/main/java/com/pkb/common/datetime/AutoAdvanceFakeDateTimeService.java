package com.pkb.common.datetime;

import java.time.Clock;
import java.time.temporal.TemporalUnit;

/**
 * Override of FakeDateTimeService that supports automatically advancing
 * time by a fixed amount each time the services is asked for a time.
 * DO NOT USE in E2E under any circumstances, it will cause extreme
 * flakiness. This is intended for use in  unit and integration tests only
 * for classes/subsystems that use temporal ordering.
 */
public class AutoAdvanceFakeDateTimeService extends FakeDateTimeService {

    private long autoAdvanceAmount = 0;
    private TemporalUnit autoAdvanceUnit = null;

    @Override
    public Clock clock() {
        if (this.currentFixedClock != null && autoAdvanceUnit != null) {
            moveTime(autoAdvanceAmount, autoAdvanceUnit);
        }
        return super.clock();
    }

    public void setAutoAdvanceDuration(long autoAdvanceAmount, TemporalUnit autoAdvanceUnit) {
        this.autoAdvanceAmount = autoAdvanceAmount;
        this.autoAdvanceUnit = autoAdvanceUnit;
    }

    public void clearAutoAdvance() {
        this.autoAdvanceAmount = 0;
        this.autoAdvanceUnit = null;
    }
}

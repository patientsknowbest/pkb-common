package com.pkb.common.datetime;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

public interface DateTimeService {

    default Instant now() {
        ZonedDateTime.ofInstant(clock().instant(), clock().getZone());
        return clock().instant();
    }

    Clock clock();

    default ZonedDateTime firstDayOfMonth() {
        ZonedDateTime zdt = ZonedDateTime.now(clock()).with(ChronoField.DAY_OF_MONTH, 1)
                .with(ChronoField.MILLI_OF_SECOND, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.HOUR_OF_DAY, 0);
        return zdt;
    }

}

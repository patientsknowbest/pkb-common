package com.pkb.common.datetime;

import java.time.Clock;

public class DefaultDateTimeService implements DateTimeService {

    @Override public Clock clock() {
        return Clock.systemDefaultZone();
    }

}

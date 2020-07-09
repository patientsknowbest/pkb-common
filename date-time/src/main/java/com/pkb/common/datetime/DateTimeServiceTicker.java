package com.pkb.common.datetime;

import com.google.common.base.Ticker;

public class DateTimeServiceTicker extends Ticker {

    private final DateTimeService dateTimeService;

    public DateTimeServiceTicker(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public long read() {
        return dateTimeService.nowNanoTime();
    }
}

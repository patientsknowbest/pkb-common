package com.pkb.common.datetime;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class DateTimeServiceTest {

    @Test
    public void firstDayOfMonth() {
        FakeDateTimeService underTest = new FakeDateTimeService();
        underTest.setCurrentTime("2010-10-10T22:22:22Z");

        Instant actual = underTest.firstDayOfMonth().toInstant();
        ZonedDateTime asd = actual.atZone(ZoneId.of("UTC"));

        Instant expected = Instant.parse("2010-10-01T00:00:00Z");
        assertEquals(expected, actual);
    }

}

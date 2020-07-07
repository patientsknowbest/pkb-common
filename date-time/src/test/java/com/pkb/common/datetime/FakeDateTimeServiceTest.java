package com.pkb.common.datetime;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;


public class FakeDateTimeServiceTest {

    private FakeDateTimeService underTest = new FakeDateTimeService();

    @Test
    public void setFixedCurrentTimeForTesting() {
        //GIVEN
        FakeDateTimeService underTest = new FakeDateTimeService();
        Instant before = Instant.now();
        //WHEN
        Instant actualNotFixed = underTest.now();
        Instant after = Instant.now();
        underTest.setFixedCurrentTimeForTesting("2019-04-01T11:12:13+00:00");
        Instant actualFixed = underTest.now();
        //THEN
        Instant expected = ZonedDateTime.of(2019, 4, 1, 11, 12, 13, 0, ZoneId.of("UTC")).toInstant();
        assertThat(actualNotFixed, greaterThanOrEqualTo(before));
        assertThat(actualNotFixed, lessThanOrEqualTo(after));
        assertThat(actualFixed, comparesEqualTo(expected));
    }

    @Test
    public void nowNanoTimeWithFixedTime() {
        //GIVEN
        //WHEN
        underTest.setFixedCurrentTimeForTesting("2020-07-06T16:41:13.431631445+00:00");
        long actual = underTest.nowNanoTime();
        //THEN
        assertThat(actual, is(1594053673431631445L));
    }

    @Test
    public void nowNanoTimeWithoutFixedTime() {
        //GIVEN
        long before = Instant.now().toEpochMilli();
        //WHEN
        long actual = underTest.nowNanoTime();
        //THEN
        long after = Instant.now().toEpochMilli();

        assertThat(TimeUnit.NANOSECONDS.toMillis(actual), greaterThanOrEqualTo(before));
        assertThat(TimeUnit.NANOSECONDS.toMillis(actual), lessThanOrEqualTo(after));
    }
}
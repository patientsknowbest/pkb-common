package com.pkb.common.datetime;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

class AutoAdvanceFakeDateTimeServiceTest {

    private AutoAdvanceFakeDateTimeService underTest = new AutoAdvanceFakeDateTimeService();

    @Test
    void testAdvancesTimeBetweenCalls() {
        underTest.setFixedCurrentTimeForTesting("2020-01-01T00:00:00Z");
        underTest.setAutoAdvanceDuration(Duration.of(1, ChronoUnit.DAYS));

        assertThat(underTest.now(), equalTo(Instant.parse("2020-01-02T00:00:00Z")));
        assertThat(underTest.now(), equalTo(Instant.parse("2020-01-03T00:00:00Z")));
    }

    @Test
    void testStopsAdvancingWhenCleared() {
        underTest.setFixedCurrentTimeForTesting("2020-01-01T00:00:00Z");
        underTest.setAutoAdvanceDuration(Duration.of(1, ChronoUnit.DAYS));

        assertThat(underTest.now(), equalTo(Instant.parse("2020-01-02T00:00:00Z")));
        underTest.setAutoAdvanceDuration(null);
        assertThat(underTest.now(), equalTo(Instant.parse("2020-01-02T00:00:00Z")));
    }

    @Test
    void testDoesNothingIfTimeNotFixed() throws InterruptedException {

        underTest.setAutoAdvanceDuration(Duration.of(1, ChronoUnit.DAYS));

        Instant start = underTest.now();
        Thread.sleep(1);
        Instant end = underTest.now();


        assertThat(start, not(end));
        assertThat(Duration.between(start, end), lessThan(Duration.of(1, ChronoUnit.SECONDS)));
    }

}

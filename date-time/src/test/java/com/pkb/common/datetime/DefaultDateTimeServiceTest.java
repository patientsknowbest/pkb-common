package com.pkb.common.datetime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThrows;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameBeanAs;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

@SuppressWarnings("ThrowableNotThrown")
@ExtendWith(MockitoExtension.class)
public class DefaultDateTimeServiceTest {

    @InjectMocks
    private DefaultDateTimeService underTest;

    @Test
    public void setFixedCurrentTimeForTesting() {
        //GIVEN
        var expected = new IllegalStateException("Not currently in a test environment");
        //WHEN-THEN
        assertThrows(sameBeanAs(expected), () -> underTest.setFixedCurrentTimeForTesting("2010-10-10T22:22:22Z"));
    }

    @Test
    public void moveTime() {
        //GIVEN
        var expected = new IllegalStateException("Not currently in a test environment");
        //WHEN-THEN
        assertThrows(sameBeanAs(expected), () -> underTest.moveTime(1L, ChronoUnit.SECONDS));
    }

    @Test
    public void forgetFixedCurrentTimeForTesting() {
        //GIVEN
        var expected = new IllegalStateException("Not currently in a test environment");
        //WHEN-THEN
        assertThrows(sameBeanAs(expected), () -> underTest.forgetFixedCurrentTimeForTesting());
    }

    @Test
    public void now() {
        //GIVEN
        Instant before = Instant.now();
        //WHEN
        Instant actual = underTest.now();
        Instant after = Instant.now();
        //THEN
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void today() {
        //GIVEN
        LocalDate before = LocalDate.now();
        //WHEN
        LocalDate actual = underTest.today();
        //THEN
        LocalDate after = LocalDate.now();
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void tomorrow() {
        //GIVEN
        LocalDate before = LocalDate.now().plusDays(1L);
        //WHEN
        LocalDate actual = underTest.tomorrow();
        //THEN
        LocalDate after = LocalDate.now().plusDays(1L);
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void yesterday() {
        //GIVEN
        LocalDate before = LocalDate.now().minusDays(1L);
        //WHEN
        LocalDate actual = underTest.yesterday();
        //THEN
        LocalDate after = LocalDate.now().minusDays(1L);
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void todayAtZoneId() {
        //GIVEN
        LocalDate before = LocalDate.now();
        //WHEN
        LocalDate actual = underTest.todayAtZoneId(ZoneId.systemDefault());
        //THEN
        LocalDate after = LocalDate.now();
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void nowZoned() {
        //GIVEN
        ZonedDateTime before = ZonedDateTime.now();
        //WHEN
        ZonedDateTime actual = underTest.nowZoned(ZoneId.systemDefault());
        //THEN
        ZonedDateTime after = ZonedDateTime.now();
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void tomorrowAtStartOfDay() {
        //GIVEN
        ZonedDateTime before = LocalDate.now().atStartOfDay().plusDays(1L).atZone(ZoneId.systemDefault()).withEarlierOffsetAtOverlap();
        //WHEN
        ZonedDateTime actual = underTest.tomorrowAtStartOfDay(ZoneId.systemDefault());
        //THEN
        ZonedDateTime after = LocalDate.now().atStartOfDay().plusDays(1L).atZone(ZoneId.systemDefault()).withEarlierOffsetAtOverlap();
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void tomorrowAtStartOfDayAsLocalDateShouldMatchTomorrow() {
        //GIVEN
        LocalDate before = LocalDate.now().plusDays(1L);
        //WHEN
        LocalDate actual = underTest.tomorrowAtStartOfDay(ZoneId.systemDefault()).toLocalDate();
        //THEN
        LocalDate after = LocalDate.now().plusDays(1L);
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void nowLocalDateTime() {
        //GIVEN
        LocalDateTime before = LocalDateTime.now();
        //WHEN
        LocalDateTime actual = underTest.nowLocalDateTime();
        //THEN
        LocalDateTime after = LocalDateTime.now();
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void nowZonedDateTime() {
        //GIVEN
        ZonedDateTime before = ZonedDateTime.now();
        //WHEN
        ZonedDateTime actual = underTest.nowZonedDateTime();
        //THEN
        ZonedDateTime after = ZonedDateTime.now();
        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }

    @Test
    public void nowNanoTime() {
        //GIVEN
        long before = System.nanoTime();
        //WHEN
        long actual = underTest.nowNanoTime();
        //THEN
        long after = System.nanoTime();

        assertThat(actual, greaterThanOrEqualTo(before));
        assertThat(actual, lessThanOrEqualTo(after));
    }
}
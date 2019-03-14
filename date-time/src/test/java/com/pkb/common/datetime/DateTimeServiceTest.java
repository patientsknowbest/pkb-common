package com.pkb.common.datetime;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.text.ParsePosition;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import io.vavr.Tuple2;

@RunWith(DataProviderRunner.class)
public class DateTimeServiceTest {

    private static final ZoneId DUTCH = ZoneId.of("Europe/Amsterdam");
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Test
    public void firstDayOfMonth() {
        FakeDateTimeService underTest = new FakeDateTimeService();
        underTest.setFixedCurrentTimeForTesting("2010-10-10T22:22:22Z");

        Instant actual = underTest.firstDayOfMonth().toInstant();
        ZonedDateTime asd = actual.atZone(UTC);

        Instant expected = Instant.parse("2010-10-01T00:00:00Z");
        assertEquals(expected, actual);
    }

    @DataProvider
    public static Object[][] lenientDateParseTestCases() {
        return new Object[][] {
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("nl", "NL")).withZone(DUTCH),
                        ZonedDateTime.of(2018, 10, 30, 8, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(DUTCH),
                        ZonedDateTime.of(2018, 10, 30, 8, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("nl", "NL")).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00 bla bla",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00:13",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00:13",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 13, 0, UTC)
                },
                {
                        "30/10/2018 09:00:13 bla bla",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 13, 0, UTC)
                }
        };
    }

    @UseDataProvider("lenientDateParseTestCases")
    @Test
    public void parseToDateBackwardCompatibleWay(String toParse, DateTimeFormatter formatter, ZonedDateTime expected) {
        FakeDateTimeService underTest = new FakeDateTimeService();

        Tuple2<Date, ParsePosition> actual = underTest.parseToDateBackwardCompatibleWay(toParse, formatter);

        assertThat(actual._1, is(Date.from(expected.toInstant())));
    }

    @DataProvider
    public static Object[][] strictDateParseTestCases() {
        return new Object[][] {
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("nl", "NL")).withZone(DUTCH),
                        ZonedDateTime.of(2018, 10, 30, 8, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(DUTCH),
                        ZonedDateTime.of(2018, 10, 30, 8, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("nl", "NL")).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC)
                },
                {
                        "30/10/2018 09:00:13",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.UK).withZone(UTC),
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 13, 0, UTC)
                }
        };
    }

    @UseDataProvider("strictDateParseTestCases")
    @Test
    public void parseToDateStrict(String toParse, DateTimeFormatter formatter, ZonedDateTime expected) {
        FakeDateTimeService underTest = new FakeDateTimeService();

        Date actual = underTest.parseToDateStrict(toParse, formatter);

        assertThat(actual, is(Date.from(expected.toInstant())));
    }

    @DataProvider
    public static Object[][] dateToStringTestCases() {
        return new Object[][] {
                {
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(UTC),
                        "30/10/2018 09:00"
                },
                {
                        ZonedDateTime.of(2018, 10, 30, 8, 0, 0, 0, UTC),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("nl", "NL")).withZone(DUTCH),
                        "30/10/2018 09:00"
                },
                {
                        ZonedDateTime.of(2018, 10, 30, 8, 0, 0, 0, UTC),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.UK).withZone(DUTCH),
                        "30/10/2018 09:00"
                },
                {
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 0, 0, UTC),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("nl", "NL")).withZone(UTC),
                        "30/10/2018 09:00"
                },
                {
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 13, 0, UTC),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.UK).withZone(UTC),
                        "30/10/2018 09:00:13"
                },
                {
                        ZonedDateTime.of(2018, 10, 30, 9, 0, 13, 0, UTC),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.UK).withZone(UTC),
                        "30/10/2018"
                },
        };
    }

    @UseDataProvider("dateToStringTestCases")
    @Test
    public void parseToDateStrict(ZonedDateTime toSerialize, DateTimeFormatter formatter, String expected) {
        FakeDateTimeService underTest = new FakeDateTimeService(null);

        String actual = underTest.dateToString(Date.from(toSerialize.toInstant()), formatter);

        assertThat(actual, is(expected));
    }
}

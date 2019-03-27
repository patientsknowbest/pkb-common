package com.pkb.common.datetime;

import java.text.ParsePosition;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import io.vavr.Tuple2;

@SuppressWarnings({ "UseOfObsoleteDateTimeApi", "SSBasedInspection" })
public interface DateTimeService {

    Clock clock();

    /**
     * @param isoZonedDateTime
     *            formatted ISO format date for a fixed "now"
     * @see java.time.ZonedDateTime#parse(CharSequence) for format
     * @throws IllegalStateException
     *             outside of testing environments
     */
    void setFixedCurrentTimeForTesting(String isoZonedDateTime);

    void moveTime(long amountToAdd, TemporalUnit unit);

    /**
     * @throws IllegalStateException
     *             outside of testing environments
     */
    void forgetFixedCurrentTimeForTesting();

    default Instant now() {
        return clock().instant();
    }

    default LocalDate today() {
        return todayAtZoneId(clock().getZone());
    }

    default LocalDate tomorrow() {
        return today().plusDays(1L);
    }

    default LocalDate yesterday() {
        return today().minusDays(1L);
    }

    default LocalDate todayAtZoneId(ZoneId zoneId) {
        return ZonedDateTime.ofInstant(clock().instant(), zoneId).toLocalDate();
    }

    default LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(clock());
    }

    default ZonedDateTime nowZonedDateTime() {
        return ZonedDateTime.now(clock());
    }

    default ZonedDateTime firstDayOfMonth() {
        return ZonedDateTime.now(clock()).with(ChronoField.DAY_OF_MONTH, 1)
                .with(ChronoField.MILLI_OF_SECOND, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.HOUR_OF_DAY, 0);
    }

    /**
     * DO NOT USE THIS! Only here temporarily to fix up some legacy (static) code.
     * @param input
     * @param formatter
     * @return
     */
    static Tuple2<Instant, ParsePosition> parseToInstantBackwardCompatibleWayStatic(String input, DateTimeFormatter formatter){
        ParsePosition remainder = new ParsePosition(0);
        return new Tuple2<>(Instant.from(formatter.parse(input, remainder)), remainder);
    }

    default Tuple2<Instant, ParsePosition> parseToInstantBackwardCompatibleWay(String input, DateTimeFormatter formatter) {
        return parseToInstantBackwardCompatibleWayStatic(input,formatter);
    }

    /**
     * @deprecated Use {@link java.time} instead of {@link Date}, then there is no need for this method.
     * @param input
     * @param formatter
     * @return
     */
    @Deprecated
    default String dateToString(Date input, DateTimeFormatter formatter) {
        return input.toInstant().atZone(ZoneId.systemDefault()).format(formatter);
    }

    default LocalDateTime convertNowToLocalDateAtTimezone(ZoneId zoneId) {
        return LocalDateTime.ofInstant(now(), zoneId);
    }

    default ZonedDateTime ofLocalDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(localDateTime, zoneId);
    }

    default LocalDate convertToLocalDateAtUTC(Date date) {
        return convertToLocalDateAtZoneId(date, ZoneOffset.UTC);
    }

    default LocalDate convertToLocalDateAtZoneId(Date date, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), zoneId).toLocalDate();
    }

    default LocalTime convertNowToLocalTimeAtZoneId(ZoneId zoneId) {
        return LocalTime.from(now().atZone(zoneId));
    }
}

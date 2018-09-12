package com.pkb.common.datetime;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Date;

import io.vavr.Tuple2;

public interface DateTimeService {

    default Instant now() {
        ZonedDateTime.ofInstant(clock().instant(), clock().getZone());
        return clock().instant();
    }

    default LocalDate today() {
        return ZonedDateTime.ofInstant(clock().instant(), clock().getZone()).toLocalDate();
    }

    Clock clock();

    default LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(clock());
    }

    default ZonedDateTime nowZonedDateTime() {
        return ZonedDateTime.now(clock());
    }

    default ZonedDateTime firstDayOfMonth() {
        ZonedDateTime zdt = ZonedDateTime.now(clock()).with(ChronoField.DAY_OF_MONTH, 1)
                .with(ChronoField.MILLI_OF_SECOND, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.HOUR_OF_DAY, 0);
        return zdt;
    }

    default LocalDate convertToLocalDateAtUTC(Date date) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC).toLocalDate();
    }

    /**
     * Parses the given {@link String} into a {@link Date} with the given {@link DateTimeFormatter}, the same way as
     * {@link SimpleDateFormat} did.
     * Backward compatibility in this case means not all of the input {@link String} have to be parsed, anything after a valid date will be
     * ignored.
     * 
     * @param input
     *            The {@link String} to parse
     * @param formatter
     *            The {@link DateTimeFormatter} to use
     * @return {@link Tuple2} of the parsed {@link Date} and the remainder index of the input string {@link ParsePosition}.
     * @throws DateTimeParseException
     *             - if unable to parse the requested result
     * @deprecated Use {@link java.time} instead of {@link Date}, then there is no need for this method.
     */
    @Deprecated
    default Tuple2<Date, ParsePosition> parseToDateBackwardCompatibleWay(String input, DateTimeFormatter formatter) {
        ParsePosition remainder = new ParsePosition(0);
        return new Tuple2<>(Date.from(Instant.from(formatter.parse(input, remainder))), remainder);
    }

    /**
     * Parses the given {@link String} into a {@link Date} with the given {@link DateTimeFormatter}, almost the same way as
     * {@link SimpleDateFormat} did. All of the input {@link String} must be parsed.
     * 
     * @param input
     *            The {@link String} to parse
     * @param formatter
     *            The {@link DateTimeFormatter} to use
     * @return Parsed {@link Date}
     * @throws DateTimeParseException
     *             - if unable to parse the requested result
     * @deprecated Use {@link java.time} instead of {@link Date}, then there is no need for this method.
     */
    @Deprecated
    default Date parseToDateStrict(String input, DateTimeFormatter formatter) {
        return Date.from(Instant.from(formatter.parse(input)));
    }

    @Deprecated
    default String dateToString(Date input, DateTimeFormatter formatter) {
        return input.toInstant().atZone(ZoneId.systemDefault()).format(formatter);
    }
}

package com.pkb.pkbcommon.ageutil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class AgeUtil {

    public final static int MINIMUM_AGE_LETTER_INVITATION = 16;

    public static boolean patientIsOlderThan(Optional<LocalDate> dateOfBirth, Instant now, int ageInYears) {
        return dateOfBirth.map(dob -> dob.plus(ageInYears, ChronoUnit.YEARS))
                .map(dob -> dob.atStartOfDay(ZoneOffset.UTC).toInstant())
                .map(milestoneBirthday -> !milestoneBirthday.isAfter(now))
                .orElse(false);
    }

    public static boolean patientIsOlderThan(LocalDate dateOfBirth, Instant now, int ageInYears) {
        return dateOfBirth.plus(ageInYears, ChronoUnit.YEARS)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .isBefore(now);
    }
}

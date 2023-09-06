package com.pkb.pkbcommon.ageutil;

import com.pkb.common.datetime.DateTimeService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class AgeUtilService {

    private final DateTimeService dateTimeService;

    public AgeUtilService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public boolean patientIsOlderThan(LocalDate dateOfBirth, int ageInYears) {
        return dateOfBirth.plus(ageInYears, ChronoUnit.YEARS)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .isBefore(dateTimeService.now());
    }
}

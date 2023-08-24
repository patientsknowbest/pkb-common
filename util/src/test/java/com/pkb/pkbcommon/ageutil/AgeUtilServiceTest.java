package com.pkb.pkbcommon.ageutil;

import com.pkb.common.datetime.DateTimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static java.time.ZoneOffset.UTC;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgeUtilServiceTest {

    @Mock
    DateTimeService dateTimeService;
    @InjectMocks
    AgeUtilService underTest;

    @Test
    public void testUnderSixteen() {
        Instant currentTime = Instant.parse("2020-01-02T00:00:00Z");
        when(dateTimeService.now()).thenReturn(currentTime);
        LocalDate dateOfBirth = currentTime.atZone(UTC).minus(16, ChronoUnit.YEARS).toLocalDate();
        assertThat(underTest.patientIsOlderThan(dateOfBirth, 16), is(false));
    }

    @Test
    public void testOverSixteen() {
        Instant currentTime = Instant.parse("2020-01-02T00:00:00Z");
        when(dateTimeService.now()).thenReturn(currentTime);
        LocalDate dateOfBirth = currentTime.atZone(UTC).minus(16, ChronoUnit.YEARS).minus(1, ChronoUnit.DAYS).toLocalDate();
        assertThat(underTest.patientIsOlderThan(dateOfBirth, 16), is(true));
    }

}

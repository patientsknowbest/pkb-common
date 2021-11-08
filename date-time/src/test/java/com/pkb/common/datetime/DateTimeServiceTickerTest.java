package com.pkb.common.datetime;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class DateTimeServiceTickerTest {

    @Mock
    DateTimeService dateTimeService;

    @InjectMocks
    DateTimeServiceTicker underTest;

    @Test
    void nowNanoTimeWithoutFixedTime() {
        //GIVEN
        BDDMockito.doReturn(13L).when(dateTimeService).nowNanoTime();
        //WHEN
        long actual = underTest.read();
        //THEN
        assertThat(actual, is(13L));
    }
}
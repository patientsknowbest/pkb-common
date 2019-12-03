package com.pkb.common.ehrdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import io.prometheus.client.Summary;

public class EHRDataXmlTimersTest {

    @Test
    public void xmlTimerPrometheus_start_startsPrometheusTimer()
    {
        //given
        Summary mockSummary = Mockito.mock(Summary.class);
        Summary.Timer mockTimer = Mockito.mock(Summary.Timer.class);
        when(mockSummary.startTimer()).thenReturn(mockTimer);

        EHRDataXmlTimers.XmlTimerPrometheus xmlTimerPrometheus = new EHRDataXmlTimers.XmlTimerPrometheus(mockSummary);

        //when
        AutocloseableTimer autocloseableTimer = xmlTimerPrometheus.startTimer();

        //then
        verify(mockSummary, times(1)).startTimer();
        assertThat(autocloseableTimer, is(xmlTimerPrometheus));
    }

    @Test
    public void xmlTimerPrometheus_observeDuration_observesPrometheusTimer() {
        //given
        Summary mockSummary = Mockito.mock(Summary.class);
        Summary.Timer mockTimer = Mockito.mock(Summary.Timer.class);
        when(mockSummary.startTimer()).thenReturn(mockTimer);
        when(mockTimer.observeDuration()).thenReturn(3.0);

        EHRDataXmlTimers.XmlTimerPrometheus xmlTimerPrometheus = new EHRDataXmlTimers.XmlTimerPrometheus(mockSummary);
        AutocloseableTimer autocloseableTimer = xmlTimerPrometheus.startTimer();

        //when
        double observedDuration = xmlTimerPrometheus.observeDuration();

        //then
        verify(mockTimer, times(1)).observeDuration();
        assertThat(observedDuration, is(3.0));
    }

    @Test
    public void xmlTimerPrometheus_close_callsObserve() {
        //given
        Summary mockSummary = Mockito.mock(Summary.class);
        Summary.Timer mockTimer = Mockito.mock(Summary.Timer.class);
        when(mockSummary.startTimer()).thenReturn(mockTimer);

        EHRDataXmlTimers.XmlTimerPrometheus xmlTimerPrometheus = spy(new EHRDataXmlTimers.XmlTimerPrometheus(mockSummary));
        AutocloseableTimer autocloseableTimer = xmlTimerPrometheus.startTimer();

        //when
        xmlTimerPrometheus.close();

        //then
        verify(xmlTimerPrometheus, times(1)).observeDuration();
    }
}
package com.pkb.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.pkb.common.datetime.FakeDateTimeService;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class DateTimeTestSupportServletTest {

    @Mock
    private FakeDateTimeService dateTimeService;

    @InjectMocks
    private DateTimeTestSupportServlet underTest;

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Test
    public void doGetWritesCurrentTimeInResponse() throws Exception {

        // GIVEN
        PrintWriter writer = mock(PrintWriter.class);
        when(resp.getWriter()).thenReturn(writer);
        String dateTime = "2019-08-13T14:00:00+00:00";
        when(dateTimeService.now()).thenReturn(ZonedDateTime.parse(dateTime).toInstant());

        // WHEN
        underTest.doGet(req, resp);

        // THEN
        verify(writer, times(1)).println(ZonedDateTime.parse(dateTime).toInstant());
    }

    @Test
    public void doPutSetsFixedTime() {

        // GIVEN
        String dateTime = "2019-08-13T14:00:00+00:00";
        when(req.getParameter("dateTime")).thenReturn(dateTime);

        // WHEN
        underTest.doPut(req, resp);

        // THEN
        verify(dateTimeService, times(1)).setFixedCurrentTimeForTesting(dateTime);
    }

    @Test
    public void doPutReturns404OnIllegalStateException() {

        // GIVEN
        String dateTime = "2019-08-13T14:00:00+00:00";
        when(req.getParameter("dateTime")).thenThrow(IllegalStateException.class);

        // WHEN
        underTest.doPut(req, resp);

        // THEN
        verify(resp, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}

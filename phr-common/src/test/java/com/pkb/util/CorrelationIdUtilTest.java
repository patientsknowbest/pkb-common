package com.pkb.util;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameBeanAs;
import static com.pkb.util.Constants.NHS_LOGIN_CORRELATION_ID;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CorrelationIdUtilTest {

    @Spy
    private CorrelationIdUtil underTest = new CorrelationIdUtil();

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testCorrelationIdAsUUIDIsPutAndRead() {
        // GIVEN
        String actual = randomUUID().toString();
        underTest.put(actual);

        // WHEN
        String expected = underTest.get();

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    public void testCorrelationIdAsUUIDIsPutOnlyIfAbsentAndRead() {
        // GIVEN
        String actual = randomUUID().toString();
        underTest.putIfAbsent(actual);

        // WHEN
        String expected = underTest.get();

        // THEN
        assertThat(expected, sameBeanAs(actual));

        // WHEN
        UUID anotherUUID = randomUUID();
        underTest.putIfAbsent(anotherUUID);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    public void testCorrelationIdAsUUIDIsRemovedAfterPut() {
        // GIVEN
        String actual = randomUUID().toString();
        underTest.put(actual);

        // WHEN
        String expected = underTest.get();

        // THEN
        assertThat(expected, sameBeanAs(actual));

        // WHEN
        underTest.remove();
        expected = underTest.get();

        // THEN
        assertThat(null, sameBeanAs(expected));
    }

    @Test
    public void testCorrelationIdFromRequest() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        UUID actual = randomUUID();
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(NHS_LOGIN_CORRELATION_ID)).thenReturn(actual.toString());

        // WHEN
        UUID expected = underTest.getCorrelationIdFromRequest(request);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    public void testCorrelationIdFromRequestWithWrongUUIDFormat() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        UUID actual = randomUUID();
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(NHS_LOGIN_CORRELATION_ID)).thenReturn("no-uuid");
        when(underTest.getAlternativeUUID()).thenReturn(actual);

        // WHEN
        UUID expected = underTest.getCorrelationIdFromRequest(request);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    public void testCorrelationIdFromRequestWithoutAttribute() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        UUID actual = randomUUID();
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(NHS_LOGIN_CORRELATION_ID)).thenReturn(null);
        when(underTest.getAlternativeUUID()).thenReturn(actual);

        // WHEN
        UUID expected = underTest.getCorrelationIdFromRequest(request);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

}

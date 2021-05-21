package com.pkb.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameBeanAs;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CorrelationIdUtilTest {

    @Spy
    private CorrelationIdUtil underTest = new CorrelationIdUtil();

    @BeforeEach
    void setup() {
        MDC.clear();
    }

    @Test
    void testCorrelationIdAsUUIDIsPutAndRead() {
        // GIVEN
        UUID actual = randomUUID();
        underTest.put(actual);

        // WHEN
        UUID expected = underTest.get();

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    void testCorrelationIdAsUUIDIsPutOnlyIfAbsentAndRead() {
        // GIVEN
        UUID actual = randomUUID();
        underTest.putIfAbsent(actual);

        // WHEN
        UUID expected = underTest.get();

        // THEN
        assertThat(expected, sameBeanAs(actual));

        // WHEN
        UUID anotherUUID = randomUUID();
        underTest.putIfAbsent(anotherUUID);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    void testCorrelationIdAsUUIDIsRemovedAfterPut() {
        // GIVEN
        UUID actual = randomUUID();
        underTest.put(actual);

        // WHEN
        UUID expected = underTest.get();

        // THEN
        assertThat(expected, sameBeanAs(actual));

        // WHEN
        underTest.remove();
        expected = underTest.get();

        // THEN
        assertThat(null, sameBeanAs(expected));
    }

    @Test
    void testCorrelationIdFromRequest() {
        // GIVEN
        UUID actual = randomUUID();

        // WHEN
        UUID expected = underTest.getCorrelationIdFromStringOrGenerateNewOne(actual.toString());

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    void testCorrelationIdFromRequestWithWrongUUIDFormat() {
        // GIVEN
        UUID actual = randomUUID();
        when(underTest.getAlternativeUUID()).thenReturn(actual);

        // WHEN
        UUID expected = underTest.getCorrelationIdFromStringOrGenerateNewOne("no-uuid");

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    void testCorrelationIdFromNullString() {
        // GIVEN
        UUID actual = randomUUID();
        when(underTest.getAlternativeUUID()).thenReturn(actual);

        // WHEN
        UUID expected = underTest.getCorrelationIdFromStringOrGenerateNewOne(null);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    void testCorrelationIdFromRequestHeader() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID actual = randomUUID();
        when(request.getHeader("X-Request-ID")).thenReturn(actual.toString());

        // WHEN
        UUID expected = underTest.getCorrelationIdFromRequest(request);

        // THEN
        assertThat(expected, sameBeanAs(actual));
    }

    @Test
    void testCorrelationIdFromRequestHeaderOverridesSessionAttribute() {

        // If a request id is present both in the header and the session, we use the one in the session

        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID actual = randomUUID();
        when(request.getHeader("X-Request-ID")).thenReturn(actual.toString());

        // WHEN
        UUID expected = underTest.getCorrelationIdFromRequest(request);

        // THEN
        assertThat(expected, sameBeanAs(actual));
        verify(request, never()).getSession();
    }

}

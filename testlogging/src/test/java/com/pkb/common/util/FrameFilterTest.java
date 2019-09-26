package com.pkb.common.util;

import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameJsonAsApproved;

import java.util.regex.Pattern;

import org.junit.Test;

import com.pkb.interceptor.FrameFilterTestHelper;

public class FrameFilterTest {

    @Test
    public void testFrameFilterExcludesNonPkbAndInterceptorCode() {
        // GIVEN - WHEN
        Exception testException = null;
        try {
            foo();
        } catch (Exception e) {
            testException = e;
        }

        // THEN
        assertThat(FrameFilter.filter(testException), sameJsonAsApproved().ignoring("0x1.stackTrace.lineNumber"));
    }

    @Test
    public void testFrameFilterAlwaysIncludesRootFrame() {
        // GIVEN - WHEN
        Exception testException = null;
        try {
            foo();
        } catch (Exception e) {
            testException = e;
        }

        // THEN
        assertThat(FrameFilter.filter(testException, Pattern.compile("^*$")), sameJsonAsApproved().ignoring("0x1.stackTrace.lineNumber"));
    }

    private static void foo() throws Exception {
        FrameFilterTestHelper.baz();
    }

    public static void bar() throws Exception {
        throw new Exception();
    }

}

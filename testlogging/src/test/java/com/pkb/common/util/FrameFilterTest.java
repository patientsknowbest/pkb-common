package com.pkb.common.util;

import com.pkb.interceptor.FrameFilterTestHelper;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameJsonAsApproved;

public class FrameFilterTest {

    //2f4ff9
    @Test
    public void testFrameFilterExcludesNonPkbAndInterceptorCode() {
        // GIVEN
        Exception testException = null;
        try {
            foo();
        } catch (Exception e) {
            testException = e;
        }
        //WHEN
        var actual = FrameFilter.filter(testException);
        // THEN
        assertThat(actual, sameJsonAsApproved());
        assertThat(actual.getStackTrace(), sameJsonAsApproved().withUniqueId("stacktrace").ignoring("lineNumber"));
    }

    // 647ec2
    @Test
    public void testFrameFilterAlwaysIncludesRootFrame() {
        // GIVEN
        Exception testException = null;
        try {
            foo();
        } catch (Exception e) {
            testException = e;
        }
        // WHEN
        var actual = FrameFilter.filter(testException, Pattern.compile("^*$"));
        // THEN
        assertThat(actual, sameJsonAsApproved());
        assertThat(actual.getStackTrace(), sameJsonAsApproved().withUniqueId("stacktrace").ignoring("lineNumber"));
    }

    private static void foo() throws Exception {
        FrameFilterTestHelper.baz();
    }

    public static void bar() throws Exception {
        throw new Exception();
    }

}

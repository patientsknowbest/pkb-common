package com.pkb.common.testlogging;

import java.util.function.Predicate;

public class NoopTestLoggingService implements TestLoggingService {
    @Override
    public void setCurrentTest(String test) {}

    @Override
    public void logForTest(Predicate<CharSequence> testNameMatcher, String log, Object... params) {}
}

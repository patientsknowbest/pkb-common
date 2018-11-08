package com.pkb.common.testlogging;

import java.util.function.Predicate;

public interface TestLoggingService {
    void setCurrentTest(String test);
    void logForTest(Predicate<CharSequence> testNameMatcher, String log, Object... params);
}

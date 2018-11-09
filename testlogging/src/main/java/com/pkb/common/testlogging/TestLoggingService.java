package com.pkb.common.testlogging;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;

public interface TestLoggingService {
    void setCurrentTest(String test);
    void logForTest(Predicate<String> testNameMatcher, String log, Object... params);

    static <T> String collectionToDebugString(Collection<T> objects) {
        return objects.stream()
                .map(ToStringBuilder::reflectionToString)
                .collect(Collectors.joining(", "));
    }
}

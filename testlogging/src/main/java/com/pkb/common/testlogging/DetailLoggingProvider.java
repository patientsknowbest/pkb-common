package com.pkb.common.testlogging;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;

public interface DetailLoggingProvider {
    Logger obtainLogger(Class<?> clazz);

    static <T> String collectionToDebugString(Collection<T> objects) {
        return objects.stream()
                .map(ToStringBuilder::reflectionToString)
                .collect(Collectors.joining(", "));
    }

    void setDetailedLoggingRequired(boolean detailedLoggingRequired);
}

package com.pkb.common.testlogging;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DetailLoggingProvider {
    Logger obtainLogger(Class<?> clazz);

    static <T> @NotNull String collectionToDebugString(@Nullable Collection<T> objects) {
        if (objects == null) {
            return "null";
        } else {
            return objects.stream()
                    .map(ToStringBuilder::reflectionToString)
                    .collect(Collectors.joining(", "));
        }
    }

    static <T> @NotNull String nullSafeReflectionToString(@Nullable T object) {
        return object == null ? "null" : ToStringBuilder.reflectionToString(object);
    }

    void setDetailedLoggingRequired(boolean detailedLoggingRequired);
}

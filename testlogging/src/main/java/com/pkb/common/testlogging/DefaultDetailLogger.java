package com.pkb.common.testlogging;

import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DefaultDetailLogger implements DetailLogger {

    private final Logger logger;

    DefaultDetailLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(Supplier<String> msgSupplier) {
        logger.info(msgSupplier.get());
    }

    @Override
    public <T> void info(String msg, @Nullable Iterable<T> objects) {
        logger.info(msg, collectionToDebugString(objects));
    }

    @Override
    public <T> void info(String msg, @Nullable T object) {
        logger.info(msg, nullSafeReflectionToString(object));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, nullSafeReflectionToString(arg1), nullSafeReflectionToString(arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        Object[] inputs = Arrays.stream(arguments).map(this::nullSafeReflectionToString).toArray();
        logger.info(format, inputs);
    }

    private <T> @NotNull String nullSafeReflectionToString(@Nullable T object) {
        return object == null ? "null" : ToStringBuilder.reflectionToString(object);
    }

    private <T> @NotNull String collectionToDebugString(@Nullable Iterable<T> objects) {
        if (objects == null) {
            return "null";
        } else {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(objects.iterator(), 0), false)
                    .map(ToStringBuilder::reflectionToString)
                    .collect(Collectors.joining(", "));
        }
    }
}

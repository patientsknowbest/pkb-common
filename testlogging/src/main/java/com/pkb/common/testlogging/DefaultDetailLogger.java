package com.pkb.common.testlogging;

import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        logger.info(msg, Objects.toString(object));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, Objects.toString(arg1), Objects.toString(arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        Object[] inputs = Arrays.stream(arguments).map(object -> Objects.toString(object)).toArray();
        logger.info(format, inputs);
    }

    private <T> @NotNull String collectionToDebugString(@Nullable Iterable<T> objects) {
        if (objects == null) {
            return "null";
        } else {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(objects.iterator(), 0), false)
                    .map(Objects::toString)
                    .collect(Collectors.joining(", "));
        }
    }
}

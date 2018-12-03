package com.pkb.common.testlogging;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

public interface DetailLogger {

    void info(String msg);

    void info(Supplier<String> msgSupplier);

    <T> void info(String msg, @Nullable Iterable<T> objects);

    <T> void info(String msg, @Nullable T object);

    void info(String format, @Nullable Object arg1, @Nullable Object arg2);

    void info(String format, Object... arguments);
}

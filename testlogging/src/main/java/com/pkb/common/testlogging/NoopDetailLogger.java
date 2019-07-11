package com.pkb.common.testlogging;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

public class NoopDetailLogger implements DetailLogger {

    @Override
    public void info(String msg) {

    }

    @Override
    public void info(Supplier<String> msgSupplier) {

    }

    @Override
    public <T> void info(String msg, @Nullable Iterable<T> objects) {

    }

    @Override
    public <T> void info(String msg, @Nullable T objects) {

    }

    @Override
    public void info(String format, @Nullable Object arg1, @Nullable Object arg2) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }
}

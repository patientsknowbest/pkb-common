package com.pkb.common.thread;

import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class UncaughtExceptionHandlingThreadFactory extends CustomizableThreadFactory {
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    public UncaughtExceptionHandlingThreadFactory(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public @NotNull Thread newThread(@NotNull Runnable runnable) {
        Thread thread = super.newThread(runnable);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        return thread;
    }
}

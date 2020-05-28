package com.pkb.common.thread;

import static java.lang.String.format;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

public class UncaughtExceptionLogger implements Thread.UncaughtExceptionHandler {
    private final Logger LOGGER = getLogger(lookup().lookupClass());

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        LOGGER.error(format("Uncaught exception occured on thread=[%s]. Details:", thread.getName()), exception);
    }
}

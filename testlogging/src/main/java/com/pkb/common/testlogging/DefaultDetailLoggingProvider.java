package com.pkb.common.testlogging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultDetailLoggingProvider implements DetailLoggingProvider {
    public static final DefaultDetailLoggingProvider INSTANCE = new DefaultDetailLoggingProvider();
    private static final DetailLogger NOOP_LOGGER = new NoopDetailLogger();
    private volatile boolean isDetailedLoggingRequired = false;

    @Override
    public DetailLogger obtainLogger(Class<?> clazz) {
        if (isDetailedLoggingRequired) {
            Logger logger = LoggerFactory.getLogger(clazz);
            return new DefaultDetailLogger(logger);
        }
        return NOOP_LOGGER;
    }

    @Override
    public DetailLogger obtainLogger(Logger logger) {
        if (isDetailedLoggingRequired) {
            return new DefaultDetailLogger(logger);
        }
        return NOOP_LOGGER;
    }

    @Override
    public void setDetailedLoggingRequired(boolean detailedLoggingRequired) {
        isDetailedLoggingRequired = detailedLoggingRequired;
    }
}

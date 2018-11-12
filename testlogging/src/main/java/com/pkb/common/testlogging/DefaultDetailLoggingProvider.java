package com.pkb.common.testlogging;


import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultDetailLoggingProvider implements DetailLoggingProvider {
    public static final DefaultDetailLoggingProvider INSTANCE = new DefaultDetailLoggingProvider();
    private volatile boolean isDetailedLoggingRequired = false;

    @Override
    public Logger obtainLogger(Class<?> clazz) {
        if (isDetailedLoggingRequired) {
            return Logger.getLogger(clazz.getName());
        } else {
            Logger noopLogger = Logger.getLogger("NOOP_LOGGER");
            noopLogger.setLevel(Level.OFF);
            return noopLogger;
        }
    }

    @Override
    public void setDetailedLoggingRequired(boolean detailedLoggingRequired) {
        isDetailedLoggingRequired = detailedLoggingRequired;
    }
}

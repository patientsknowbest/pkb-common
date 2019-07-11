package com.pkb.common.testlogging;

import org.slf4j.Logger;

public interface DetailLoggingProvider {
    DetailLogger obtainLogger(Class<?> clazz);

    DetailLogger obtainLogger(Logger logger);

    void setDetailedLoggingRequired(boolean detailedLoggingRequired);

}

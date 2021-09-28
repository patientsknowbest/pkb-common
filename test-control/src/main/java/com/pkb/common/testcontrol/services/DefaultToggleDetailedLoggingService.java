package com.pkb.common.testcontrol.services;

import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.common.testcontrol.message.DetailedLoggingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultToggleDetailedLoggingService implements ToggleDetailedLoggingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private final DetailLoggingProvider testLoggingService;

    public DefaultToggleDetailedLoggingService(DetailLoggingProvider testLoggingService) {
        this.testLoggingService = testLoggingService;
    }

    @Override
    public void process(DetailedLoggingRequest message) {
        LOGGER.info("ToggleDetailedLoggingService.process message received");
        testLoggingService.setDetailedLoggingRequired(message.enableDetailedLogging());
        LOGGER.info("ToggleDetailedLoggingService.process done.");
    }
}

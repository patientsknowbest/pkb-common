package com.pkb.common.testsupport.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.config.BaseConfig;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.pubsub.testsupport.payload.ToggleDetailedLoggingRequest;
import com.pkb.pubsub.testsupport.payload.ToggleDetailedLoggingResponse;

public class ToggleDetailedLoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private final DetailLoggingProvider testLoggingService;
    private final BaseConfig config;

    public ToggleDetailedLoggingService(BaseConfig config, DetailLoggingProvider testLoggingService) {
        this.config = config;
        this.testLoggingService = testLoggingService;
    }

    public ToggleDetailedLoggingResponse process(ToggleDetailedLoggingRequest message) {
        LOGGER.info("ToggleDetailedLoggingService.process message received");
        boolean success = true;
        if (config.isFakeDateTimeServiceEnabled()) {
            testLoggingService.setDetailedLoggingRequired(message.getEnableDetailedLogging());
        } else {
            success = false;
        }

        LOGGER.info("ToggleDetailedLoggingService.process done.");

        return ToggleDetailedLoggingResponse.newBuilder().setSuccess(success).build();
    }
}

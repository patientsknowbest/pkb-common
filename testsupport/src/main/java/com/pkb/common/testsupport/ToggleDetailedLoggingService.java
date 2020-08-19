package com.pkb.common.testsupport;

import com.pkb.common.config.BaseConfig;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.pulsar.payload.ToggleDetailedLoggingRequest;
import com.pkb.pulsar.payload.ToggleDetailedLoggingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ToggleDetailedLoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private final DetailLoggingProvider testLoggingService;
    private final BaseConfig config;

    ToggleDetailedLoggingService(BaseConfig config, DetailLoggingProvider testLoggingService) {
        this.config = config;
        this.testLoggingService = testLoggingService;
    }

    ToggleDetailedLoggingResponse process(ToggleDetailedLoggingRequest message) {
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

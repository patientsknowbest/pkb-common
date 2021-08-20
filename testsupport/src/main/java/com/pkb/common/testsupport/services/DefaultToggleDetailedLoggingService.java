package com.pkb.common.testsupport.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.config.BaseConfig;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.pubsub.testsupport.payload.ToggleDetailedLoggingRequest;
import com.pkb.pubsub.testsupport.payload.ToggleDetailedLoggingResponse;

public class DefaultToggleDetailedLoggingService implements ToggleDetailedLoggingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private final DetailLoggingProvider testLoggingService;

    public DefaultToggleDetailedLoggingService(DetailLoggingProvider testLoggingService) {
        this.testLoggingService = testLoggingService;
    }

    @Override
    public ToggleDetailedLoggingResponse process(ToggleDetailedLoggingRequest message) {
        LOGGER.info("ToggleDetailedLoggingService.process message received");
        testLoggingService.setDetailedLoggingRequired(message.getEnableDetailedLogging());
        LOGGER.info("ToggleDetailedLoggingService.process done.");
        return ToggleDetailedLoggingResponse.newBuilder().setSuccess(true).build();
    }
}

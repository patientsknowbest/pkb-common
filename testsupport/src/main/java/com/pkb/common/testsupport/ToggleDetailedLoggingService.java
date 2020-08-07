package com.pkb.common.testsupport;

import com.pkb.pulsar.payload.ToggleDetailedLoggingRequest;
import com.pkb.pulsar.payload.ToggleDetailedLoggingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleDetailedLoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    public ToggleDetailedLoggingService() {
    }

    public ToggleDetailedLoggingResponse process(ToggleDetailedLoggingRequest message) {
        LOGGER.info("InjectConfigValueService.process message received");

        // TODO JAMES: do some stuff here

        LOGGER.info("InjectConfigValueService.process done.");

        return ToggleDetailedLoggingResponse.newBuilder().build();
    }
}

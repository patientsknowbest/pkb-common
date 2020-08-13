package com.pkb.common.testsupport;

import com.pkb.common.config.ConfigStorage;
import com.pkb.pulsar.payload.LogTestNameRequest;
import com.pkb.pulsar.payload.LogTestNameResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTestNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final ConfigStorage config;

    public LogTestNameService(ConfigStorage config) {
        this.config = config;
    }

    public LogTestNameResponse process(LogTestNameRequest message) {
        LOGGER.info("LogTestNameService.process message received");
        String testName = message.getTestName();
        boolean success = true;
        if (config == null || config.isFakeDateTimeServiceEnabled()) {
            LOGGER.info("test name is now {}", testName);
        } else {
            success = false;
        }

        LOGGER.info("LogTestNameService.process done.");

        return LogTestNameResponse.newBuilder().setSuccess(success).build();
    }
}

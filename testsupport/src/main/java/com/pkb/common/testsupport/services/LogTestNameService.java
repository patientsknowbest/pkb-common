package com.pkb.common.testsupport.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.config.BaseConfig;
import com.pkb.pulsar.payload.LogTestNameRequest;
import com.pkb.pulsar.payload.LogTestNameResponse;

public class LogTestNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final BaseConfig config;

    public LogTestNameService(BaseConfig config) {
        this.config = config;
    }

    public LogTestNameResponse process(LogTestNameRequest message) {
        LOGGER.info("LogTestNameService.process message received");
        String testName = message.getTestName();
        boolean success = true;
        if (config.isFakeDateTimeServiceEnabled()) {
            LOGGER.info("test name is now {}", testName);
        } else {
            success = false;
        }

        LOGGER.info("LogTestNameService.process done.");

        return LogTestNameResponse.newBuilder().setTestName(testName).setSuccess(success).build();
    }
}

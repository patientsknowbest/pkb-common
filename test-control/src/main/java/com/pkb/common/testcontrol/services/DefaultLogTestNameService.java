package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.LogTestNameRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogTestNameService implements LogTestNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    @Override
    public void process(LogTestNameRequest message) {
        LOGGER.info("LogTestNameService.process message received");
        String testName = message.testName();
        LOGGER.info("test name is now {}", testName);
        LOGGER.info("LogTestNameService.process done.");
    }
}

package com.pkb.common.testsupport.services;

import com.pkb.pubsub.testsupport.payload.LogTestNameRequest;
import com.pkb.pubsub.testsupport.payload.LogTestNameResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogTestNameService implements LogTestNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    @Override
    public LogTestNameResponse process(LogTestNameRequest message) {
        LOGGER.info("LogTestNameService.process message received");
        String testName = message.getTestName();
        LOGGER.info("test name is now {}", testName);
        LOGGER.info("LogTestNameService.process done.");
        return LogTestNameResponse.newBuilder().setTestName(testName).setSuccess(true).build();
    }
}

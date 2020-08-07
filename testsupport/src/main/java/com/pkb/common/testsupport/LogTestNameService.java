package com.pkb.common.testsupport;

import com.pkb.pulsar.payload.LogTestNameRequest;
import com.pkb.pulsar.payload.LogTestNameResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTestNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    public LogTestNameService( ) {

    }

    public LogTestNameResponse process(LogTestNameRequest message) {
        LOGGER.info("InjectConfigValueService.process message received");

        // TODO JAMES: do something here

        LOGGER.info("InjectConfigValueService.process done.");

        // TODO JAMES: set a return value
        return LogTestNameResponse.newBuilder().build();
    }
}

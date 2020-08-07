package com.pkb.common.testsupport;

import com.pkb.pulsar.payload.ClearTestStatesRequest;
import com.pkb.pulsar.payload.ClearTestStatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearTestStatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    public ClearTestStatesService() {
    }

    public ClearTestStatesResponse process(ClearTestStatesRequest message) {
        LOGGER.info("InjectConfigValueService.process message received");

        // TODO JAMES: do something here

        LOGGER.info("InjectConfigValueService.process done.");

        // TODO JAMES: set a response value
        return ClearTestStatesResponse.newBuilder().build();
    }
}

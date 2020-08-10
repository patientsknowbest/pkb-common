package com.pkb.common.testsupport;

import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.pulsar.payload.ClearTestStatesRequest;
import com.pkb.pulsar.payload.ClearTestStatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearTestStatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;

    public ClearTestStatesService(DateTimeService dateTimeService, ConfigStorage configStorage) {
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
    }

    public ClearTestStatesResponse process(ClearTestStatesRequest message) {
        LOGGER.info("ClearTestStatesService.process message received");

        boolean clearFixedTimestamp = message.getClearFixedTimestamp();
        if (clearFixedTimestamp) {
            dateTimeService.forgetFixedCurrentTimeForTesting();
        }

        configStorage.reset();

        // TODO: add a TestSupportAgent to the KMS so that we reset the state there
        // RESTMessagingHelper.kmsDeleteCall(appGroup, "/kmstest/state", 204);

        // TODO JAMES: add something to do the survey results in reportrunner
        // given().delete(appGroup.reportRunnerUri() + "/survey_results").then().statusCode(204);

        LOGGER.info("ClearTestStatesService.process done.");

        // TODO JAMES: set a response value
        return ClearTestStatesResponse.newBuilder().build();
    }
}

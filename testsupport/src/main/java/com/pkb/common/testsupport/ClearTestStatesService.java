package com.pkb.common.testsupport;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.pulsar.payload.ClearTestStatesRequest;
import com.pkb.pulsar.payload.ClearTestStatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ClearTestStatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;
    private final Set<ClearableInternalState> clearables;

    public ClearTestStatesService(DateTimeService dateTimeService, ConfigStorage configStorage, Set<ClearableInternalState> clearables) {
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
        this.clearables = clearables;
    }

    public ClearTestStatesResponse process(ClearTestStatesRequest message) {
        LOGGER.info("ClearTestStatesService.process message received");

        boolean clearFixedTimestamp = message.getClearFixedTimestamp();
        if (clearFixedTimestamp) {
            dateTimeService.forgetFixedCurrentTimeForTesting();
        }

        // TODO JAMES : move report runner (and KMS?) to ConfigStorage, so this will always be present
        if (configStorage != null) {
            configStorage.reset();
        }

        for (ClearableInternalState clearable : clearables) {
            clearable.clearState();
        }

        // TODO: add a TestSupportAgent to the KMS so that we reset the state there
        // RESTMessagingHelper.kmsDeleteCall(appGroup, "/kmstest/state", 204);

        // TODO JAMES: add something to do the survey results in reportrunner
        // given().delete(appGroup.reportRunnerUri() + "/survey_results").then().statusCode(204);

        LOGGER.info("ClearTestStatesService.process done.");

        // TODO JAMES: set a response value
        return ClearTestStatesResponse.newBuilder().build();
    }
}

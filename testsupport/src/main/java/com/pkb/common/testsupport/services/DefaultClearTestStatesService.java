package com.pkb.common.testsupport.services;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.pubsub.testsupport.payload.ClearTestStatesRequest;
import com.pkb.pubsub.testsupport.payload.ClearTestStatesResponse;

public class DefaultClearTestStatesService implements ClearTestStatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;
    private final Set<ClearableInternalState> clearables;

    public DefaultClearTestStatesService(DateTimeService dateTimeService, ConfigStorage configStorage, Set<ClearableInternalState> clearables) {
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
        this.clearables = clearables;
    }

    @Override
    public ClearTestStatesResponse process(ClearTestStatesRequest message) {
        LOGGER.info("ClearTestStatesService.process message received");

        boolean clearFixedTimestamp = message.getClearFixedTimestamp();
        if (clearFixedTimestamp) {
            dateTimeService.forgetFixedCurrentTimeForTesting();
        }

        if (configStorage.isMutableConfigEnabled()) {
            configStorage.reset();
        }

        clearables.forEach(ClearableInternalState::clearState);

        LOGGER.info("ClearTestStatesService.process done.");

        return ClearTestStatesResponse.newBuilder().build();
    }
}

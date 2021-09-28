package com.pkb.common.testcontrol.services;

import java.util.Set;

import com.pkb.common.testcontrol.message.ClearStatesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;

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
    public void process(ClearStatesRequest message) {
        LOGGER.info("ClearTestStatesService.process message received");

        boolean clearFixedTimestamp = message.clearFixedTimestamp();
        if (clearFixedTimestamp) {
            dateTimeService.forgetFixedCurrentTimeForTesting();
        }

        if (configStorage.isMutableConfigEnabled()) {
            configStorage.reset();
        }

        clearables.forEach(ClearableInternalState::clearState);

        LOGGER.info("ClearTestStatesService.process done.");
    }
}

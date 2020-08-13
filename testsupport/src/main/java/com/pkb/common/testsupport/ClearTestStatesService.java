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

        // Move report runner (PHR-6985) and KMS (PHR-6984) to ConfigStorage
        // ConfigStorage should then be enforced as NotNull and the null check removed
        if (configStorage != null && configStorage.isMutableConfigEnabled()) {
            configStorage.reset();
        }

        clearables.forEach(ClearableInternalState::clearState);

        LOGGER.info("ClearTestStatesService.process done.");

        return ClearTestStatesResponse.newBuilder().build();
    }
}

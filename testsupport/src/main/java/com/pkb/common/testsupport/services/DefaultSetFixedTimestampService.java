package com.pkb.common.testsupport.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import com.pkb.common.datetime.DateTimeService;
import com.pkb.pubsub.testsupport.payload.SetFixedTimestampRequest;
import com.pkb.pubsub.testsupport.payload.SetFixedTimestampResponse;

public class DefaultSetFixedTimestampService implements SetFixedTimestampService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private DateTimeService dateTimeService;

    public DefaultSetFixedTimestampService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public SetFixedTimestampResponse process(SetFixedTimestampRequest message) {
        LOGGER.info("SetFixedTimestampService.process received message");
        String timestamp = message.getTimestamp();
        LOGGER.info(String.format("SetFixedTimestampService.process timestamp=%s", timestamp));
        if (Strings.isNullOrEmpty(timestamp)) {
            dateTimeService.forgetFixedCurrentTimeForTesting();
        }
        else {
            dateTimeService.setFixedCurrentTimeForTesting(timestamp);
        }
        LOGGER.info("SetFixedTimestampService.process done.");
        return SetFixedTimestampResponse.newBuilder().setTimestamp(timestamp).build();
    }
}

package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.FixTimeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import com.pkb.common.datetime.DateTimeService;

public class DefaultSetFixedTimestampService implements SetFixedTimestampService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private DateTimeService dateTimeService;

    public DefaultSetFixedTimestampService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public void process(FixTimeRequest message) {
        LOGGER.info("SetFixedTimestampService.process received message");
        String timestamp = message.timestamp();
        LOGGER.info(String.format("SetFixedTimestampService.process timestamp=%s", timestamp));
        if (Strings.isNullOrEmpty(timestamp)) {
            dateTimeService.forgetFixedCurrentTimeForTesting();
        }
        else {
            dateTimeService.setFixedCurrentTimeForTesting(timestamp);
        }
        LOGGER.info("SetFixedTimestampService.process done.");
    }
}

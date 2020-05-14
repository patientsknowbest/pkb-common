package com.pkb.common.testsupport;

import com.google.common.base.Strings;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.pulsar.payload.SetFixedTimestampRequest;
import com.pkb.pulsar.payload.SetFixedTimestampResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetFixedTimestampService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private DateTimeService dateTimeService;

    public SetFixedTimestampService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

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

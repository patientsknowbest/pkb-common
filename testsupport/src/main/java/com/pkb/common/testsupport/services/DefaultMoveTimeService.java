package com.pkb.common.testsupport.services;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.datetime.DateTimeService;
import com.pkb.pubsub.testsupport.payload.MoveTimeRequest;
import com.pkb.pubsub.testsupport.payload.MoveTimeResponse;

public class DefaultMoveTimeService implements MoveTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private DateTimeService dateTimeService;

    public DefaultMoveTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public MoveTimeResponse process(MoveTimeRequest message) {
        LOGGER.info("MoveTimeService.process received message");
        long amount = message.getAmount();
        TemporalUnit unit = ChronoUnit.valueOf(message.getUnit());
        LOGGER.info("MoveTimeService.process moving by {} {}", amount, unit);
        dateTimeService.moveTime(amount, unit);
        LOGGER.info("MoveTimeService.process done.");
        return MoveTimeResponse.newBuilder().setAmount(amount).setUnit(unit.toString()).build();
    }
}

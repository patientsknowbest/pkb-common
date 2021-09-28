package com.pkb.common.testcontrol.services;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import com.pkb.common.testcontrol.message.MoveTimeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.datetime.DateTimeService;

public class DefaultMoveTimeService implements MoveTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private DateTimeService dateTimeService;

    public DefaultMoveTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public void process(MoveTimeRequest message) {
        LOGGER.info("MoveTimeService.process received message");
        long amount = message.amount();
        TemporalUnit unit = ChronoUnit.valueOf(message.unit());
        LOGGER.info("MoveTimeService.process moving by {} {}", amount, unit);
        dateTimeService.moveTime(amount, unit);
        LOGGER.info("MoveTimeService.process done.");
    }
}

package com.pkb.common.testlogging;

import java.lang.invoke.MethodHandles;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultTestLoggingService implements TestLoggingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final DefaultTestLoggingService INSTANCE = new DefaultTestLoggingService();

    private String currentTest = "";

    @Override
    public void setCurrentTest(String test) {
        this.currentTest = test;
        LOGGER.info("Current test is now {}", this.currentTest);
    }

    @Override
    public void logForTest(Predicate<String> testNameMatcher, String log, Object... params) {
        if (testNameMatcher.test(currentTest)) {
            LOGGER.info(log, params);
        }
    }
}

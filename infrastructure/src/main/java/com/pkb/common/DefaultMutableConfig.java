package com.pkb.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.pkb.unit.Bus;
import com.pkb.unit.Unit;

/**
 * A <a href="https://github.com/patientsknowbest/unit">unit</a> for storing runtime-mutable configuration
 *  TODO: MFA - this should probably be in it's own unit-extensions lib,
 *  not in pkb-common
 */
public class DefaultMutableConfig extends Unit implements MutableConfig {
    public static final String UNIT_ID = DefaultMutableConfig.class.getName();
    private Map<String, String> config = new HashMap<>();

    public DefaultMutableConfig(Bus bus) {
        // Retry doesn't really matter here, it can't fail.
        super(UNIT_ID, bus, 1, TimeUnit.SECONDS);
    }

    @Override
    protected HandleOutcome handleStart() {
        return HandleOutcome.SUCCESS;
    }

    @Override
    protected HandleOutcome handleStop() {
        return HandleOutcome.SUCCESS;
    }

    @Override
    public void setConfig(String key, String value) {
        config.put(key, value);
        stop(); // force dependencies to restart after a config change
    }

    @Override
    public String getConfig(String key) {
        return config.get(key);
    }
}

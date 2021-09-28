package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.message.InjectConfigRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.config.ConfigStorage;

public class DefaultInjectConfigValueService implements InjectConfigValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private ConfigStorage configStorage;

    public DefaultInjectConfigValueService(ConfigStorage configStorage) {
        this.configStorage = configStorage;
    }

    @Override
    public void process(InjectConfigRequest message) {
        if (configStorage.isMutableConfigEnabled()) {
            var key = message.key();
            var value = message.value();
            LOGGER.info(String.format("InjectConfigValueService.process setting key %s to value %s", key, value));
            configStorage.setValue(key, value);
        } else {
            LOGGER.error("Inject config request received but mutable config is not enabled!");
        }
    }
}

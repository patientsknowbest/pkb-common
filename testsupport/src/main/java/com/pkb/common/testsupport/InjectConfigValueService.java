package com.pkb.common.testsupport;

import com.pkb.common.config.ConfigStorage;
import com.pkb.pulsar.payload.InjectConfigRequest;
import com.pkb.pulsar.payload.InjectConfigResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjectConfigValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private ConfigStorage configStorage;

    public InjectConfigValueService(ConfigStorage configStorage) {
        this.configStorage = configStorage;
    }

    public InjectConfigResponse process(InjectConfigRequest message) {
        LOGGER.info("InjectConfigValueService.process message received");
        String key = message.getKey();
        String value = message.getValue();
        LOGGER.info(String.format("InjectConfigValueService.process setting key %s to value %s", key, value));

        boolean success = true;

        try {
            if (configStorage.isMutableConfigEnabled()) {
                configStorage.setValue(key, value);
            }
        } catch (IllegalStateException e) {
            success = false;
        }

        LOGGER.info("InjectConfigValueService.process done.");
        return InjectConfigResponse.newBuilder().setKey(key).setValue(value).setSuccess(success).build();
    }
}
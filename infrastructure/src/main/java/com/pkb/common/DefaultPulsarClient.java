package com.pkb.common;

import static com.pkb.unit.Unit.HandleOutcome.FAILURE;
import static com.pkb.unit.Unit.HandleOutcome.SUCCESS;
import static java.lang.String.format;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.config.RawConfigStorage;
import com.pkb.unit.Bus;
import com.pkb.unit.Unit;

/**
 * Several services depend on Apache Pulsar, provide a Unit wrapper around it.
 * TODO: MFA - this should probably be in it's own unit-extensions lib,
 * not in pkb-common
 */
public class DefaultPulsarClient extends Unit implements PulsarClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String UNIT_ID = DefaultPulsarClient.class.getName();
    private final RawConfigStorage rawConfigStorage;
    private final MutableConfig mutableConfig;

    private org.apache.pulsar.client.api.PulsarClient pulsarClient;

    public DefaultPulsarClient(Bus bus,
                               RawConfigStorage rawConfigStorage,
                               MutableConfig mutableConfig) {
        super(UNIT_ID, bus, 1, TimeUnit.SECONDS);
        this.rawConfigStorage = rawConfigStorage;
        this.mutableConfig = mutableConfig;
        addDependency(DefaultMutableConfig.UNIT_ID);
    }


    @Override
    protected HandleOutcome handleStart() {
        try {
            LOGGER.info("Starting Pulsar Client");
            pulsarClient = org.apache.pulsar.client.api.PulsarClient.builder()
                    .serviceUrl(rawConfigStorage.getString(PulsarClient.PULSAR_SERVICE_URL_CONFIG_KEY))
                    .connectionTimeout(10, TimeUnit.SECONDS)
                    .operationTimeout(10, TimeUnit.SECONDS)
                    .build();
            LOGGER.info("Started Pulsar Client");
            return SUCCESS;
        } catch (PulsarClientException e) {
            LOGGER.error("Failed to start PulsarClient", e);
            return FAILURE;
        }
    }

    @Override
    protected HandleOutcome handleStop() {
        try {
            LOGGER.info("Stopping Pulsar Client");
            pulsarClient.close();
            LOGGER.info("Stopped Pulsar Client");
            return SUCCESS;
        } catch (PulsarClientException e) {
            LOGGER.error("Failed to stop PulsarClient", e);
            return FAILURE;
        }
    }

    @Override
    public <T> Producer<T> newProducer(Schema<T> schema, String topic) throws PulsarClientException {
        String namespace = mutableConfig.getConfig(PulsarClient.PULSAR_NAMESPACE_CONFIG_KEY);
        return pulsarClient.newProducer(schema)
                .topic(format("persistent://tenant/%s/%s", namespace, topic))
                .create();
    }
}

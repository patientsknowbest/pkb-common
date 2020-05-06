package com.pkb.common.testsupport;

import com.pkb.common.util.FrameFilter;
import com.pkb.pulsar.IPulsarFactory;
import org.apache.avro.specific.SpecificRecord;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PulsarFactoryWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private IPulsarFactory pulsarFactory;
    private PulsarClient pulsarClient;

    public PulsarFactoryWrapper(IPulsarFactory pulsarFactory) {
        this.pulsarFactory = pulsarFactory;
        try {
            pulsarClient = pulsarFactory.createClient();
        } catch (PulsarClientException e) {
            LOGGER.error("Unable to create PulsarClient", FrameFilter.filter(e));
            throw new RuntimeException("Unable to create PulsarClient", e);
        }
    }

    public <T extends SpecificRecord> Consumer<T> createTestControlConsumer(String topic, String subscription, Class<T> avroClass, MessageListener<T> listener)
            throws PulsarClientException {
        return pulsarFactory.testControlConsumerBuilder(pulsarClient, topic, subscription, avroClass).messageListener(listener).messageListener(listener).subscribe();
    }

    public <T extends SpecificRecord> Producer<T> createTestControlProducer(String topic, Class<T> avroClass) throws PulsarClientException {
        return pulsarFactory.testControlProducerBuilder(pulsarClient, topic, avroClass).create();
    }

    public void setApplicationNamespace(String applicationNamespace) {
        pulsarFactory.setApplicationNamespace(applicationNamespace);
    }

}

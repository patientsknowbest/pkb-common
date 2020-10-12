package com.pkb.common.testsupport;

import com.pkb.common.util.FrameFilter;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PulsarFactoryWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private PulsarClient pulsarClient;

    public PulsarFactoryWrapper( ) {
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

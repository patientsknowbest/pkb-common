package com.pkb.common;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

public interface PulsarClient {
    String PULSAR_NAMESPACE_CONFIG_KEY = "pulsarNamespace";
    String PULSAR_SERVICE_URL_CONFIG_KEY = "pulsarServiceUrl";

    <T> Producer<T> newProducer(Schema<T> schema, String topic) throws PulsarClientException;
}

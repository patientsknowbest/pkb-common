package com.pkb.common.testsupport;

import com.pkb.pulsar.payload.NamespaceChangeRequest;
import com.pkb.pulsar.payload.NamespaceChangeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamespaceChangeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private PulsarFactoryWrapper pulsarFactoryWrapper;

    public NamespaceChangeService(PulsarFactoryWrapper pulsarFactoryWrapper) {
        this.pulsarFactoryWrapper = pulsarFactoryWrapper;
    }

    public NamespaceChangeResponse process(NamespaceChangeRequest message) {
        LOGGER.info("NamespaceChangeService.process message received");
        String namespace = message.getNewNamespace();
        LOGGER.info(String.format("NamespaceChangeService.process changing namespace to %s", namespace));
        pulsarFactoryWrapper.setApplicationNamespace(namespace);
        LOGGER.info("NamespaceChangeService.process done.");
        return NamespaceChangeResponse.newBuilder().setNamespace(namespace).build();
    }
}

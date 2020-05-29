package com.pkb.common.testsupport;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.pulsar.payload.MessageType;
import com.pkb.pulsar.payload.TestControlRequest;
import com.pkb.pulsar.payload.TestControlResponse;

public class TestControlRequestService implements MessageListener<TestControlRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private static final long serialVersionUID = -1024361953131578469L;

    private final Producer<TestControlResponse> testControlResponseProducer;
    private final String serviceName;
    private final SetFixedTimestampService setFixedTimestampService;
    private final PulsarNamespaceChangeService pulsarNamespaceChangeService;

    public TestControlRequestService(
            @NotNull Producer<TestControlResponse> testControlResponseProducer,
            @NotNull String serviceName,
            @NotNull PulsarNamespaceChangeService pulsarNamespaceChangeService,
            @NotNull SetFixedTimestampService setFixedTimestampService) {
        this.testControlResponseProducer = testControlResponseProducer;
        this.serviceName = serviceName;
        this.pulsarNamespaceChangeService = pulsarNamespaceChangeService;
        this.setFixedTimestampService = setFixedTimestampService;
    }

    @Override
    public void received(Consumer<TestControlRequest> consumer, Message<TestControlRequest> message) {
        try {
            consumer.acknowledge(message); // because the testSupprot framework is exercising closed loop control we can acknowledge here
            LOGGER.info("TestControlRequestService message recieved");
            MessageType messageType = message.getValue().getMessageType();
            LOGGER.info(String.format("messageType %s", messageType));
            TestControlResponse.Builder response = TestControlResponse.newBuilder()
                    .setMessageType(messageType)
                    .setService(serviceName);
            switch (messageType) {
                case SET_NAMESPACE:
                    response.setNamespaceChangeResponse(pulsarNamespaceChangeService.process(message.getValue().getNamespaceChangeRequest()));
                    break;

                case SET_FIXED_TIMESTAMP:
                    response.setSetFixedTimestampResponse(setFixedTimestampService.process(message.getValue().getSetFixedTimestampRequest()));
                    break;
            }
            testControlResponseProducer.newMessage().value(response.build()).send();
            LOGGER.info("TestControlRequestService response sent");
        } catch (PulsarClientException e) {
            LOGGER.error("TestControlRequestService listen Exception", e);
        }
    }
}

package com.pkb.common.testsupport;

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
    private final MoveTimeService moveTimeService;
    private final NamespaceChangeService namespaceChangeService;
    private final InjectConfigValueService injectConfigValueService;
    private final ClearTestStatesService clearTestStatesService;
    private final LogTestNameService logTestNameService;
    private final ToggleDetailedLoggingService toggleDetailedLoggingService;

    public TestControlRequestService(
            @NotNull Producer<TestControlResponse> testControlResponseProducer,
            @NotNull String serviceName,
            @NotNull NamespaceChangeService namespaceChangeService,
            @NotNull SetFixedTimestampService setFixedTimestampService,
            @NotNull MoveTimeService moveTimeService,
            @NotNull InjectConfigValueService injectConfigValueService,
            @NotNull ClearTestStatesService clearTestStatesService,
            @NotNull LogTestNameService logTestNameService,
            @NotNull ToggleDetailedLoggingService toggleDetailedLoggingService) {
        this.testControlResponseProducer = testControlResponseProducer;
        this.serviceName = serviceName;
        this.namespaceChangeService = namespaceChangeService;
        this.setFixedTimestampService = setFixedTimestampService;
        this.moveTimeService = moveTimeService;
        this.injectConfigValueService = injectConfigValueService;
        this.clearTestStatesService = clearTestStatesService;
        this.logTestNameService = logTestNameService;
        this.toggleDetailedLoggingService = toggleDetailedLoggingService;
    }

    @Override
    public void received(Consumer<TestControlRequest> consumer, Message<TestControlRequest> message) {
        try {
            consumer.acknowledge(message); // because the testSupprot framework is exercising closed loop control we can acknowledge here
            LOGGER.info("TestControlRequestService message received");
            MessageType messageType = message.getValue().getMessageType();
            LOGGER.info(String.format("messageType %s", messageType));
            TestControlResponse.Builder response = TestControlResponse.newBuilder()
                    .setMessageType(messageType)
                    .setService(serviceName);
            switch (messageType) {
                case SET_NAMESPACE:
                    response.setNamespaceChangeResponse(namespaceChangeService.process(message.getValue().getNamespaceChangeRequest()));
                    break;
                case SET_FIXED_TIMESTAMP:
                    response.setSetFixedTimestampResponse(setFixedTimestampService.process(message.getValue().getSetFixedTimestampRequest()));
                    break;
                case MOVE_TIME:
                    response.setMoveTimeResponse(moveTimeService.process(message.getValue().getMoveTimeRequest()));
                    break;
                case INJECT_CONFIG_VALUE:
                    response.setInjectConfigResponse(injectConfigValueService.process(message.getValue().getInjectConfigRequest()));
                    break;
                case CLEAR_TEST_STATES:
                    response.setClearTestStatesResponse(clearTestStatesService.process(message.getValue().getClearTestStatesRequest()));
                    break;
                case LOG_TEST_NAME:
                    response.setLogTestNameResponse(logTestNameService.process(message.getValue().getLogTestNameRequest()));
                    break;
                case TOGGLE_DETAILED_LOGGING:
                    response.setToggleDetailedLoggingResponse(toggleDetailedLoggingService.process(message.getValue().getToggleDetailedLoggingRequest()));
                    break;
            }
            testControlResponseProducer.newMessage().value(response.build()).send();
            LOGGER.info("TestControlRequestService response sent");
        } catch (PulsarClientException e) {
            LOGGER.error("TestControlRequestService listen Exception", e);
        }
    }
}

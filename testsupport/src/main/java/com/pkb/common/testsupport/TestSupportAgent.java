package com.pkb.common.testsupport;

import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.util.FrameFilter;
import com.pkb.pulsar.IPulsarFactory;
import com.pkb.pulsar.payload.Startup;
import com.pkb.pulsar.payload.TestControlRequest;
import com.pkb.pulsar.payload.TestControlResponse;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static com.pkb.pulsar.PulsarConstants.STARTUP;
import static com.pkb.pulsar.PulsarConstants.TEST_CONTROL_REQUEST;
import static com.pkb.pulsar.PulsarConstants.TEST_CONTROL_RESPONSE;

public class TestSupportAgent implements ITestSupportAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final String serviceName;
    private final boolean registerStartup;
    private final boolean startListener;
    private final PulsarFactoryWrapper pulsarFactoryWrapper;
    private final DateTimeService dateTimeService;
    private Consumer<TestControlRequest> consumer;

    public TestSupportAgent(String serviceName,
                            boolean registerStartup,
                            boolean startListener,
                            IPulsarFactory pulsarFactory,
                            DateTimeService dateTimeService) {
        this.serviceName = serviceName;
        this.registerStartup = registerStartup;
        this.startListener = startListener;
        this.pulsarFactoryWrapper = new PulsarFactoryWrapper(pulsarFactory);
        this.dateTimeService = dateTimeService;

    }

    @Override
    public void start() {
        LOGGER.info("TestSupportAgent.start");
        registerStartup();
        startLstener();
    }

    private void registerStartup() {
        LOGGER.info("TestSupportAgent.registerStartup");
        try {
            if (registerStartup) {
                LOGGER.info("TestSupportAgent.registerStartup is enabled and starting");
                Producer<Startup> startupProducer = pulsarFactoryWrapper.createTestControlProducer(STARTUP, Startup.class);
                Startup message = Startup.newBuilder().setService(serviceName).build();
                startupProducer.newMessage().value(message).send();
                startupProducer.close();
                LOGGER.info(String.format("TestSupportAgent.registerStartup %s startup message sent %s", serviceName, Instant.now()));
            }
        } catch (Exception e) {
            LOGGER.error(String.format("%s registerStartup error", serviceName), FrameFilter.filter(e));
        }
        LOGGER.info(String.format("%s registerStartup done", serviceName));
    }

    private void startLstener() {
        LOGGER.info("TestSupportAgent.startListener");
        if (startListener) {
            try {
                LOGGER.info("TestSupportAgent.startListener is enabled and starting");
                TestControlRequestService service = new TestControlRequestService(
                        pulsarFactoryWrapper.createTestControlProducer(TEST_CONTROL_RESPONSE, TestControlResponse.class),
                        serviceName,
                        new PulsarNamespaceChangeService(this.pulsarFactoryWrapper),
                        new SetFixedTimestampService(dateTimeService)
                );
                consumer = pulsarFactoryWrapper.createTestControlConsumer(TEST_CONTROL_REQUEST, serviceName, TestControlRequest.class, service);
                LOGGER.info("TestSupportAgent.startListener started");
            } catch (PulsarClientException e) {
                LOGGER.error(String.format("Unable to startup %s", serviceName), FrameFilter.filter(e));
            }
        }
    }

    @Override
    public void close() {
        LOGGER.error("TestSupportAgent.close");
        if (consumer != null) {
            try {
                LOGGER.error("TestSupportAgent.close trying consumer.close()");
                consumer.close();
                LOGGER.error("TestSupportAgent.close complete");
            } catch (PulsarClientException e) {
                LOGGER.error("Unable to close consumer", e);
            }
        }
    }

}

package com.pkb.common.testsupport;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.BaseConfig;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.common.util.FrameFilter;
import com.pkb.pulsar.payload.Startup;
import com.pkb.pulsar.payload.TestControlRequest;
import com.pkb.pulsar.payload.TestControlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.pkb.pulsar.PulsarConstants.STARTUP;
import static com.pkb.pulsar.PulsarConstants.TEST_CONTROL_REQUEST;
import static com.pkb.pulsar.PulsarConstants.TEST_CONTROL_RESPONSE;

public class TestSupportAgent implements ITestSupportAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
    private static final String PROJECT_ID = "develop-238811";
    private static final String TOPIC_ID_STARTUP = "Startup";

    private final String serviceName;
    private final boolean registerStartup;
    private final boolean startListener;
    private final PulsarFactoryWrapper pulsarFactoryWrapper;
    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;
    private final BaseConfig baseConfig;
    private Subscriber subscriber;
    private final Set<ClearableInternalState> clearables;
    private final DetailLoggingProvider testLoggingService;

    public TestSupportAgent(String serviceName,
                            boolean registerStartup,
                            boolean startListener,
                            IPulsarFactory pulsarFactory,
                            DateTimeService dateTimeService,
                            ConfigStorage configStorage,
                            Set<ClearableInternalState> clearables,
                            DetailLoggingProvider testLoggingService,
                            BaseConfig baseConfig) {
        this.serviceName = serviceName;
        this.registerStartup = registerStartup;
        this.startListener = startListener;
        this.pulsarFactoryWrapper = new PulsarFactoryWrapper(pulsarFactory);
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
        this.clearables = clearables;
        this.testLoggingService = testLoggingService;
        this.baseConfig = baseConfig;
    }

    @Override
    public void start() {
        LOGGER.info("TestSupportAgent.start");
        registerStartup();
        startListener();
    }

    private void registerStartup() {
        LOGGER.info("TestSupportAgent.registerStartup");
        try {
            if (registerStartup) {
                LOGGER.info("TestSupportAgent.registerStartup is enabled and starting");

                TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID_STARTUP);

                Publisher publisher = Publisher.newBuilder(topicName).build();
                Startup message = Startup.newBuilder().setService(serviceName).build();
                PubsubMessage pubsubMessage = PubsubMessage.parseFrom(message.toByteBuffer());
                publisher.publish(pubsubMessage);

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                String messageId = messageIdFuture.get();

                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
                LOGGER.info(String.format("TestSupportAgent.registerStartup %s startup message sent message with id %s at %s", serviceName, messageId, Instant.now()));
            }
        } catch (Exception e) {
            LOGGER.error(String.format("%s registerStartup error", serviceName), FrameFilter.filter(e));
        }
        LOGGER.info(String.format("%s registerStartup done", serviceName));
    }

    private void startListener() {
        LOGGER.info("TestSupportAgent.startListener");
        if (startListener) {

            MessageReceiver receiver =
                    (PubsubMessage message, AckReplyConsumer consumer) -> {
                        // Handle incoming message, then ack the received message.
                        System.out.println("Id: " + message.getMessageId());
                        System.out.println("Data: " + message.getData().toStringUtf8());
                        consumer.ack();
                    };

            //try {
            String projectId = "develop-238811";
            String subscriptionId = TEST_CONTROL_REQUEST;
            ProjectSubscriptionName subscriptionName =
                    ProjectSubscriptionName.of(projectId, subscriptionId);

            LOGGER.info("TestSupportAgent.startListener is enabled and starting");
                subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
                // Start the subscriber.
                subscriber.startAsync().awaitRunning();
                LOGGER.info("TestSupportAgent.startListener started and listening for messages on {}", subscriptionName);
            //} catch (TimeoutException timeoutException) {
            //    LOGGER.error(String.format("Stopping subscriber %s", serviceName), FrameFilter.filter(timeoutException));
            //    subscriber.stopAsync();
            //}
                consumer = pulsarFactoryWrapper.createTestControlConsumer(TEST_CONTROL_REQUEST, serviceName, TestControlRequest.class, service);

        }
    }

    private MessageListener<TestControlRequest> getTestControlRequestService() throws PulsarClientException  {
        return new TestControlRequestService(
                pulsarFactoryWrapper.createTestControlProducer(TEST_CONTROL_RESPONSE, TestControlResponse.class),
                serviceName,
                new NamespaceChangeService(this.pulsarFactoryWrapper),
                new SetFixedTimestampService(dateTimeService),
                new MoveTimeService(dateTimeService),
                new InjectConfigValueService(configStorage),
                new ClearTestStatesService(dateTimeService, configStorage, clearables),
                new LogTestNameService(baseConfig),
                new ToggleDetailedLoggingService(baseConfig, testLoggingService)
        );
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


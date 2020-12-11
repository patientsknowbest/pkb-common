package com.pkb.common.testsupport;

import java.util.Optional;
import java.util.Set;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.pubsub.GooglePubsubComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.BaseConfig;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.pulsar.payload.MessageType;
import com.pkb.pulsar.payload.NamespaceChangeResponse;
import com.pkb.pulsar.payload.Startup;
import com.pkb.pulsar.payload.TestControlRequest;
import com.pkb.pulsar.payload.TestControlResponse;

/**
 * An instance of this needs to be injected into the camel context (application), either manually or with the relevant
 * injection framework annotation. Then the camel application will configure the endpoints and routes.
 *
 * This class could be further abstracted with a AbstractGCPTestSupportCamelRoutes.
 */
public abstract class AbstractTestSupportCamelRoutes extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    @SuppressWarnings("unused") //@EndpointInject, @Consume, @Produce aren't seen by intellij as entrypoints, you can configure it to but that's a project specific inspections file
    @EndpointInject(property = "subscriptionUri")
    private Endpoint testControlRequestSubscription;

    @SuppressWarnings("unused")
    @EndpointInject("google-pubsub:pubsub-295918:startup")
    private Endpoint startupTopic;

    @SuppressWarnings("unused")
    @EndpointInject("google-pubsub:pubsub-295918:testControlResponse")
    private Endpoint testControlResponseTopic;

    //temporary impl, will be replaced with a service once we create our first real message
    private String currentNamespace = "NOT YET SET";

    public TestControlResponse handleTestSupportRequest(TestControlRequest request) {
        MessageType messageType = request.getMessageType();
        LOGGER.info(String.format(getApplicationName() + ": handleRequest messageType %s", messageType));
        TestControlResponse.Builder response = TestControlResponse.newBuilder()
                .setMessageType(messageType)
                .setService(getApplicationName());

        switch (messageType) {
            case SET_NAMESPACE:
                this.currentNamespace = request.getNamespaceChangeRequest().getNewNamespace();
                response.setNamespaceChangeResponse(NamespaceChangeResponse.newBuilder().setNamespace(currentNamespace).build());
                break;
            case SET_FIXED_TIMESTAMP:
                response.setSetFixedTimestampResponse(getSetFixedTimestampService().process(request.getSetFixedTimestampRequest()));
                break;
            case MOVE_TIME:
                response.setMoveTimeResponse(getMoveTimeService().process(request.getMoveTimeRequest()));
                break;
            case INJECT_CONFIG_VALUE:
                response.setInjectConfigResponse(getInjectConfigValueService().process(request.getInjectConfigRequest()));
                break;
            case CLEAR_TEST_STATES:
                response.setClearTestStatesResponse(getClearTestStatesService().process(request.getClearTestStatesRequest()));
                break;
            case LOG_TEST_NAME:
                response.setLogTestNameResponse(getLogTestNameService().process(request.getLogTestNameRequest()));
                break;
            case TOGGLE_DETAILED_LOGGING:
                response.setToggleDetailedLoggingResponse(getToggleDetailedLoggingService().process(request.getToggleDetailedLoggingRequest()));
                break;
        }
        LOGGER.info(String.format(getApplicationName() + ": finished handleRequest messageType %s", messageType));
        return response.build();
    }

    /**
     * @return A unique name for the application, used to build the subscription URI
     */
    public abstract String getApplicationName();

    /**
     * @return Should the application register itself with test control.
     */
    public abstract boolean registerStartup();

    /**
     * @return Should the application listen to test control messages.
     */
    public abstract boolean startListening();

    public abstract DateTimeService getDateTimeService();

    public abstract ConfigStorage getConfigStorage();

    public abstract Set<ClearableInternalState> getClearables();

    public abstract DetailLoggingProvider getTestLoggingService();

    public abstract BaseConfig getBaseConfig();

    /**
     * Basics: https://camel.apache.org/manual/latest/java-dsl.html
     * Makes use of the Simple Expression Language: https://camel.apache.org/components/latest/languages/simple-language.html#top
     *
     * Some useful info:
     *
     * Acknowledgement of a message happens as a future task after the full exchange is completed
     *  - org.apache.camel.component.google.pubsub.consumer.CamelMessageReceiver#receiveMessage
     *
     * If the message fails with an exception, we won't ack it and we'll just retry indefinitely (until the test times out)
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {

        //configure the component - this eventually needs moving out as it should be shared across multiple routes
        GooglePubsubComponent component = (GooglePubsubComponent) getContext().getComponent("google-pubsub");
        component.setPublisherCacheTimeout(0);
        if (getEmulatorEndpoint().isPresent()) {
            component.setEndpoint(getEmulatorEndpoint().get());
        }

        //announce the app wants to receive test control messages
        if (registerStartup()) {
            from("timer:startup?repeatCount=1")
                    .routeId("startupMessage")
                    .setBody((exchange) -> Startup.newBuilder().setService(getApplicationName()).build())
                    .marshal().avro(Startup.getClassSchema())
                    .log(getApplicationName() + ": sending startup msg")
                    .to(startupTopic);
        }

        if (startListening()) {
            //requests in from test control
            from(testControlRequestSubscription)
                    .threads(1) //even though we pull messages asynchronously (much faster to read ahead), we want to process them in order so just use one thread
                    .routeId("testSupportReceiver")
                    .unmarshal().avro(TestControlRequest.getClassSchema())
                    .log(getApplicationName() + ": receiving a ${mandatoryBodyAs(" + TestControlRequest.class.getCanonicalName() + ").getMessageType} request")
                    .bean(this, "handleTestSupportRequest")
                    .log(getApplicationName() + ": sending a ${mandatoryBodyAs(" + TestControlResponse.class.getCanonicalName() + ").getMessageType} response")
                    .marshal().avro(TestControlResponse.getClassSchema())
                    .to(testControlResponseTopic)
                    .log(getApplicationName() + ": sent");
        }
    }

    private SetFixedTimestampService getSetFixedTimestampService() { return new SetFixedTimestampService(getDateTimeService()); }
    private MoveTimeService getMoveTimeService() { return new MoveTimeService(getDateTimeService()); }
    private InjectConfigValueService getInjectConfigValueService() { return new InjectConfigValueService(getConfigStorage());}
    private ClearTestStatesService getClearTestStatesService() { return new ClearTestStatesService(getDateTimeService(), getConfigStorage(), getClearables());}
    private LogTestNameService getLogTestNameService() { return new LogTestNameService(getBaseConfig());}
    private ToggleDetailedLoggingService getToggleDetailedLoggingService() { return new ToggleDetailedLoggingService(getBaseConfig(), getTestLoggingService());}

    public String getSubscriptionUri() {
        return String.format("google-pubsub:%s:testControlRequest-%s?synchronousPull=false&maxMessagesPerPoll=1", getProject(), getApplicationName());
    }

    /**
     * @return The URL of the emulator, or Optional.empty if we're running against an actual pubsub instance
     */
    public Optional<String> getEmulatorEndpoint() {
        return Optional.of("pubsub:8085");
    }

    /**
     * @return The project should we point at an actual pubsub instance, used as part of the endpoint URI. This could just be "emulator".
     */
    public String getProject()
    {
        return "emulator";
    }
}

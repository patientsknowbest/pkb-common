package com.pkb.common.testsupport.camel.route;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.common.testsupport.config.ITestControlServiceConfig;
import com.pkb.pubsub.testsupport.payload.MessageType;
import com.pkb.pubsub.testsupport.payload.NamespaceChangeResponse;
import com.pkb.pubsub.testsupport.payload.Startup;
import com.pkb.pubsub.testsupport.payload.TestControlRequest;
import com.pkb.pubsub.testsupport.payload.TestControlResponse;

/**
 * An instance of this needs to be injected into the camel context, either manually or with the relevant
 * injection framework annotation. Then the camel application will configure the endpoints and routes.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractTestSupportCamelRouteBuilder extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    @SuppressWarnings("unused") //@EndpointInject, @Consume, @Produce aren't seen by intellij as entrypoints, you can configure it to but that's a project specific inspections file
    @EndpointInject(property = "subscriptionUri") //property so bean injectors have a chance to wire the config by the time this is used
    private Endpoint testControlRequestSubscription;

    @SuppressWarnings("unused")
    @EndpointInject(property = "startupTopicUri")
    private Endpoint startupTopic;

    @SuppressWarnings("unused")
    @EndpointInject(property = "testControlResponseTopicUri")
    private Endpoint testControlResponseTopic;

    /**
     * Encapsulates the app-specific config required for these routes
     */
    public abstract ITestControlServiceConfig config();

    /**
     * synchronousPull=false for higher throughput
     */
    public String getSubscriptionUri() {
        return String.format("google-pubsub:%s:testControlRequest-%s?synchronousPull=false", config().getProject(), config().getApplicationName());
    }

    public String getStartupTopicUri() {
        return String.format("google-pubsub:%s:startup", config().getProject());
    }

    public String getTestControlResponseTopicUri() {
        return String.format("google-pubsub:%s:testControlResponse", config().getProject());
    }

    /**
     * Basics: https://camel.apache.org/manual/latest/java-dsl.html
     * Makes use of the Simple Expression Language: https://camel.apache.org/components/latest/languages/simple-language.html#top
     *
     * Some useful info:
     *
     * Acknowledgement of a message happens as a future task after the exchange is completed
     *  - org.apache.camel.component.google.pubsub.consumer.CamelMessageReceiver#receiveMessage
     *
     * If the message fails with an exception, we won't ack it and we'll just retry indefinitely
     */
    @Override
    public void configure() throws Exception {
        //announce the app is ready to receive test control messages
        if (config().getShouldRegisterStartup()) {
            from("timer:startup?repeatCount=1")
                    .routeId("startupMessage")
                    .setBody((exchange) -> Startup.newBuilder().setService(config().getApplicationName()).build())
                    .marshal().avro(Startup.getClassSchema())
                    .log(config().getApplicationName() + ": sending startup msg")
                    .to(startupTopic);
        }

        //requests in from test control
        if (config().getShouldStartListener()) {
            from(testControlRequestSubscription)
                    .threads(1) //One thread is enough
                    .routeId("testSupportReceiver")
                    .unmarshal().avro(TestControlRequest.getClassSchema())
                    .log(config().getApplicationName() + ": receiving a ${mandatoryBodyAs(" + TestControlRequest.class.getCanonicalName() + ").getMessageType} request")
                    .bean(this, "handleTestSupportRequest")
                    .log(config().getApplicationName() + ": sending a ${mandatoryBodyAs(" + TestControlResponse.class.getCanonicalName() + ").getMessageType} response")
                    .marshal().avro(TestControlResponse.getClassSchema())
                    .to(testControlResponseTopic)
                    .log(config().getApplicationName() + ": sent");
        }
    }


    public TestControlResponse handleTestSupportRequest(TestControlRequest request) {
        MessageType messageType = request.getMessageType();
        LOGGER.info(String.format(config().getApplicationName() + ": handleRequest messageType %s", messageType));
        TestControlResponse.Builder response = TestControlResponse.newBuilder()
                .setMessageType(messageType)
                .setService(config().getApplicationName());

        switch (messageType) {
            case SET_NAMESPACE:
                String newNamespace = request.getNamespaceChangeRequest().getNewNamespace();
                config().getNamespaceService().setCurrentNamespace(newNamespace);
                response.setNamespaceChangeResponse(NamespaceChangeResponse.newBuilder().setNamespace(newNamespace).build());
                break;
            case SET_FIXED_TIMESTAMP:
                response.setSetFixedTimestampResponse(config().getSetFixedTimestampService().process(request.getSetFixedTimestampRequest()));
                break;
            case MOVE_TIME:
                response.setMoveTimeResponse(config().getMoveTimeService().process(request.getMoveTimeRequest()));
                break;
            case INJECT_CONFIG_VALUE:
                response.setInjectConfigResponse(config().getInjectConfigValueService().process(request.getInjectConfigRequest()));
                break;
            case CLEAR_TEST_STATES:
                response.setClearTestStatesResponse(config().getClearTestStatesService().process(request.getClearTestStatesRequest()));
                break;
            case LOG_TEST_NAME:
                response.setLogTestNameResponse(config().getLogTestNameService().process(request.getLogTestNameRequest()));
                break;
            case TOGGLE_DETAILED_LOGGING:
                response.setToggleDetailedLoggingResponse(config().getToggleDetailedLoggingService().process(request.getToggleDetailedLoggingRequest()));
                break;
        }
        LOGGER.info(String.format(config().getApplicationName() + ": finished handleRequest messageType %s", messageType));
        return response.build();
    }
}

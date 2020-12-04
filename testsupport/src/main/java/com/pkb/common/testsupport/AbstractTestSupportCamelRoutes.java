package com.pkb.common.testsupport;

import java.util.Map;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.camel.Consume;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.pubsub.GooglePubsubConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.pulsar.payload.MoveTimeRequest;
import com.pkb.pulsar.payload.MoveTimeResponse;
import com.pkb.pulsar.payload.NamespaceChangeRequest;
import com.pkb.pulsar.payload.NamespaceChangeResponse;
import com.pkb.pulsar.payload.SetFixedTimestampRequest;
import com.pkb.pulsar.payload.SetFixedTimestampResponse;

/**
 * An instance of this needs to be injected into the camel context (application), either manually or with the relevant
 * injection framework annotation. Then the camel application will configure the endpoints and routes.
 *
 * This class could be further abstracted with a AbstractGCPTestSupportCamelRoutes.
 */
public abstract class AbstractTestSupportCamelRoutes extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    public static final String AVROCLASS_ATTRIBUTE = "avroclass";

    //TODO there'll be a nicer way of doing this
    private static final String EXPRESSION_TO_EXTRACT_AVRO_CLASS_USING_SIMPLE = "${headers[" + GooglePubsubConstants.ATTRIBUTES + "]get(" + AVROCLASS_ATTRIBUTE + ")}";

    @EndpointInject(property = "subscriptionUri")
    private Endpoint testSupportSubscription;

    @EndpointInject("google-pubsub:pubsub-295918:testTopic")
    private Endpoint testSupportTopic;

    @Produce("direct:responses")
    private ProducerTemplate avroResponse;

    private String currentNamespace = "NOT YET SET";

    @Consume("direct:ConsumeSetFixedTimestampRequest")
    public void handleSetFixedTimestampRequest(SetFixedTimestampRequest avroMessage) {
        SetFixedTimestampResponse response = getSetFixedTimestampService().process(avroMessage);
        reply(response);
    }

    @Consume("direct:ConsumeMoveTimeRequest")
    public void handleSetFixedTimestampRequest(MoveTimeRequest avroMessage) {
        MoveTimeResponse response = getMoveTimeService().process(avroMessage);
        reply(response);
    }

    @Consume("direct:ConsumeNamespaceChangeRequest")
    public void handleConsumeNamespaceChangeRequest(NamespaceChangeRequest avroMessage) {
        this.currentNamespace = avroMessage.getNewNamespace();
        NamespaceChangeResponse response = NamespaceChangeResponse.newBuilder().setNamespace(currentNamespace).build();
        reply(response);
    }

    /**
     * each instance should have a different subscription, the application name would work well.
     * @return
     */
    public abstract String getSubscriptionName();

    /**
     * The project should we point at an actual pubsub instance. This could just be "emulator".
     * @return
     */
    public abstract String getProject();

    public abstract SetFixedTimestampService getSetFixedTimestampService();

    public abstract MoveTimeService getMoveTimeService();

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
     *
     * If we receive a message for the wrong namespace, we just log it happened and ignore it
     *
     * Leaky abstractions:
     *  - PubSub just deals with raw byte arrays, we put the avro class in a message attribute
     *   which is available as a header under a pubsub specific key
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        //the main receiver for test support messages.
        from(testSupportSubscription)
            .threads(1) //even though we pull messages asynchronously (much faster to read ahead), we want to process them in order so just use one thread
            .routeId("testSupportReceiver")
            .to("log:receiving a message")
            .toD("dataformat:avro:unmarshal?instanceClassName=" + EXPRESSION_TO_EXTRACT_AVRO_CLASS_USING_SIMPLE)
            .choice()
                .when(isAvroType(NamespaceChangeRequest.class))
                    .to("direct:ConsumeNamespaceChangeRequest")
                .otherwise()
                    .choice()
                        .when(correctNamespace())
                            .choice()
                                .when(isAvroType(SetFixedTimestampRequest.class))
                                    .to("direct:ConsumeSetFixedTimestampRequest")
                                .when(isAvroType(MoveTimeRequest.class))
                                    .to("direct:ConsumeMoveTimeRequest")
                                .otherwise()
                                    .to("log: unexpected type")
                            .endChoice()
                        .otherwise()
                            .to("log: skipping incorrect namespace"); //could throw exception instead

        //dummy timer to send a message until we configure the test support agent
        from("timer:10s?period=30000")
            .routeId("dummyFixedTimestamp")
            .setBody((exchange) -> SetFixedTimestampRequest.newBuilder().setTimestamp("2020-06-05T11:12:13Z").build())
            .setHeader(GooglePubsubConstants.ATTRIBUTES, () -> Map.of("avroclass", SetFixedTimestampRequest.class.getCanonicalName(), "namespace", "newNamespace"))
            .toD("dataformat:avro:marshal?instanceClassName=" + EXPRESSION_TO_EXTRACT_AVRO_CLASS_USING_SIMPLE)
            .to("log:sending SetFixedTimestampRequest")
            .to(testSupportTopic);

        //dummy timer to send a message until we configure the test support agent
        from("timer:20s?period=20000")
            .routeId("dummyMoveTime")
            .setBody((exchange) -> MoveTimeRequest.newBuilder().setAmount(1).setUnit("MINUTES").build())
            .setHeader(GooglePubsubConstants.ATTRIBUTES, () -> Map.of("avroclass",MoveTimeRequest.class.getCanonicalName(), "namespace", "newNamespace"))
            .toD("dataformat:avro:marshal?instanceClassName=" + EXPRESSION_TO_EXTRACT_AVRO_CLASS_USING_SIMPLE)
            .to("log:sending MoveTimeRequest")
            .to(testSupportTopic);

        //dummy timer to send a message until we configure the test support agent
        from("timer:30s?period=20000&delay=30s")
            .routeId("dummyChangeNamespace")
            .setBody((exchange) -> NamespaceChangeRequest.newBuilder().setNewNamespace("newNamespace").build())
            .setHeader(GooglePubsubConstants.ATTRIBUTES, () -> Map.of("avroclass",NamespaceChangeRequest.class.getCanonicalName(), "namespace", "irrelevant here, won't be checked"))
            .toD("dataformat:avro:marshal?instanceClassName=" + EXPRESSION_TO_EXTRACT_AVRO_CLASS_USING_SIMPLE)
            .to("log:sending NamespaceChangeRequest")
            .to(testSupportTopic);

        //the response channel, dummy until we configure the testsupportagent
        from("direct:responses")
            .to("log:Dummy response log");
    }

    private Predicate isAvroType(Class<?> avroRecordType) {
        return exchange -> {
            Object avroclass = extractMessageAttributes(exchange).get("avroclass");
            return avroclass != null
                    && avroclass.equals(avroRecordType.getCanonicalName());
        };
    }

    private Predicate correctNamespace() {
        return exchange -> {
            Object requestedNamespace = extractMessageAttributes(exchange).get("namespace");
            if (requestedNamespace == null || !requestedNamespace.equals(currentNamespace)) {
                LOGGER.error("Incoming message {} has namespace {} but we're expecting {}", exchange.getIn().getBody(), requestedNamespace, currentNamespace);
                return false;
            }
            return true;
        };
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> extractMessageAttributes(Exchange exchange) {
        return (Map<String, String>)exchange.getIn().getHeader(GooglePubsubConstants.ATTRIBUTES, Map.class);
    }

    public String getSubscriptionUri() {
        return String.format("google-pubsub:%s:%s?synchronousPull=false&maxMessagesPerPoll=1", getProject(), getSubscriptionName());
    }

    public void reply(SpecificRecordBase response) {
        avroResponse.sendBody(response);
    }
}

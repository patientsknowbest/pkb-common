package com.pkb.common.pubsub;

import com.pkb.common.pubsub.config.IGeneralPubsubConfig;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;

public abstract class ConsultationCompletedRoute extends RouteBuilder {

    @EndpointInject(property = "consultationEventTopicUri")
    private Endpoint consultationEventTopic;

    public abstract IGeneralPubsubConfig config();

    public String getConsultationEventTopicUri() {
        return String.format("google-pubsub:%s:consultationEventTopic", config().getProject());
    }

    @Override
    public void configure() {
        from("direct:consultationEventTopic")
                .to(consultationEventTopic);
    }

}

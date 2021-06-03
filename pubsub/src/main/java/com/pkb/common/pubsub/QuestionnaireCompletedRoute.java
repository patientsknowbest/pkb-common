package com.pkb.common.pubsub;

import com.pkb.common.pubsub.config.IGeneralPubsubConfig;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;

public abstract class QuestionnaireCompletedRoute extends RouteBuilder {

    @EndpointInject(property = "questionnaireEventTopicUri")
    private Endpoint questionnaireEventTopic;

    public abstract IGeneralPubsubConfig config();

    public String getQuestionnaireEventTopicUri() {
        return String.format("google-pubsub:%s:questionnaireEventTopic", config().getProject());
    }

    @Override
    public void configure() {
        from("direct:questionnaireEventTopic")
                .to(questionnaireEventTopic);
    }


}

package com.pkb.common.pubsub;

import com.pkb.common.pubsub.config.IGeneralPubsubConfig;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class QuestionnaireCompletedRoute extends RouteBuilder {

    @EndpointInject(property = "questionnaireEventTopicUri")
    private Endpoint questionnaireEventTopic;

    public abstract IGeneralPubsubConfig config();

    public String getQuestionnaireEventTopicUri() {
        return String.format("google-pubsub:%s:questionnaire_event_topic%s",
                config().getProject(),
                getServiceAccountKeyURI());
    }

    private String getServiceAccountKeyURI() {
        return isBlank(config().getServiceAccountKey()) ? EMPTY :
                "?serviceAccountKey=file://" + config().getServiceAccountKey();
    }

    @Override
    public void configure() {
        from("direct:questionnaire_event_topic")
                .to(questionnaireEventTopic);
    }


}

package com.pkb.common.testsupport.camel.route;

import java.util.Map;
import java.util.Optional;

import org.apache.camel.Message;
import org.apache.camel.component.google.pubsub.GooglePubsubConstants;

public class RouteHelpers {
    public static final String NAMESPACE_HEADER_ATTRIBUTE = "namespace";
    public static final String GOOGLE_PUBSUB_COMPONENT_URI = "google-pubsub";

    public static Optional<Map<String, String>> maybeGetMessageAttributes(Message message) {
        //noinspection unchecked
        return Optional.ofNullable((Map<String, String>) message.getHeader(GooglePubsubConstants.ATTRIBUTES, Map.class));
    }
}

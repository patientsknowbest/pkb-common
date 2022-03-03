package com.pkb.common.testcontrol.camel.route;

import com.pkb.common.testcontrol.camel.InvalidNamespaceException;
import com.pkb.common.testcontrol.services.PubSubNamespaceService;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.pubsub.GooglePubsubConstants;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spi.RoutePolicy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.pkb.common.testcontrol.camel.route.RouteHelpers.GOOGLE_PUBSUB_COMPONENT_URI;
import static com.pkb.common.testcontrol.camel.route.RouteHelpers.NAMESPACE_HEADER_ATTRIBUTE;
import static com.pkb.common.testcontrol.camel.route.RouteHelpers.maybeGetMessageAttributes;

/**
 * Allows us to more easily create routes that handle test namespacing
 *  - Validates incoming pubsub messages have the correct namespace
 *  - Adds the namespace to any new messages
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractNamespaceAwareRouteBuilder extends RouteBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private static final String NAMESPACE_EXCHANGE_PROPERTY = "pkb_namespace";

    /**
     * The service responsible for keeping track of the current namespace
     */
    @NotNull
    public abstract PubSubNamespaceService camelNamespaceService();

    /**
     * Should we throw an exception if we receive a message for a different namespace to expected?
     */
    public abstract boolean throwExceptionForNamespaceMismatch();

    /**
     * The component we'll enforce the presence of a namespace on
     */
    @NotNull
    protected String getNamespaceComponentPrefix() {
        return GOOGLE_PUBSUB_COMPONENT_URI;
    }

    /**
     * The namespace should be added as early as possible in the lifetime of an exchange so it more accurately matches the namespace
     * at the point of production rather than the namespace at the point of transmission. Usually headers are copied across
     * from the incoming message by the framework, but in case any processors recreate headers etc, it's possible to re-call
     * this and it'll set the namespace based on a temporary exchange property set on exchange start.
     */
    @NotNull
    protected Processor addNamespace() {
        return exchange -> {
            Message message = exchange.getMessage();
            String tempEndpointProperty = (String) exchange.getProperties().computeIfAbsent(NAMESPACE_EXCHANGE_PROPERTY,
                    (k) -> camelNamespaceService().getCurrentNamespace());
            Map<String, String> attributes = maybeGetMessageAttributes(message).orElse(new HashMap<>());
            message.setHeader(GooglePubsubConstants.ATTRIBUTES, io.vavr.collection.HashMap.ofAll(attributes)
                    .put(NAMESPACE_HEADER_ATTRIBUTE, tempEndpointProperty, (current, ignored) -> current)
                    .toJavaMap());
        };
    }

    /**
     * Called by Camel for each from()
     * Could possibly use a ValidatorBuilder instead
     */
    @Override
    protected void configureRoute(RouteDefinition route) {
        configureExceptionHandler(route);

        configureNamespaceRoutePolicy(route);
    }

    protected void configureExceptionHandler(RouteDefinition route) {
        String fromUri = route.getInput().getEndpointUri();

        if (fromUri.startsWith(getNamespaceComponentPrefix())) {
            // disable redelivery attempts for namespace exceptions
            // The point of entry for redeliveries is the point of failure:
            // https://camel.apache.org/manual/latest/exception-clause.html#ExceptionClause-PointofEntryforRedeliveryAttempts
            // This causes issues with our NamespaceRoutePolicy as the framework doesn't recall the 'begin' stage of the exchange.
            // Instead, the dead letter topic should be triggered immediately.
            
            // TODO: MFA - Actually, mark it has handled and just log it, so google pubsub is acked
            // and never retried at all (because google pubsub has it's own retry policy)
            //route.onException(InvalidNamespaceException.class).maximumRedeliveries(0);
            route.onException(InvalidNamespaceException.class)
                    .logHandled(true)
                    .handled(true)
                    .to("log:ignoredMessages?level=INFO");
        }
    }

    protected void configureNamespaceRoutePolicy(RouteDefinition route) {
        String fromUri = route.getInput().getEndpointUri();
        route.routePolicy(new NamespaceRoutePolicy(fromUri));
    }

    private class NamespaceRoutePolicy implements RoutePolicy {
        private final String fromUri;

        public NamespaceRoutePolicy(String fromUri) {
            this.fromUri = fromUri;
        }

        @Override
        public void onInit(Route route) {}

        @Override
        public void onRemove(Route route) {}

        @Override
        public void onStart(Route route) {}

        @Override
        public void onStop(Route route) {}

        @Override
        public void onSuspend(Route route) {}

        @Override
        public void onResume(Route route) {}

        @Override
        public void onExchangeBegin(Route route, Exchange exchange) {

            //any incoming routes from pubsub should have a namespace. If they don't, the sender has been configured incorrectly.
            if (fromUri.startsWith(getNamespaceComponentPrefix())) {
                String messageNamespace = maybeGetMessageAttributes(exchange.getIn())
                        .map(attributes -> attributes.getOrDefault(NAMESPACE_HEADER_ATTRIBUTE, "missing"))
                        .orElse("no pubsub attributes");

                String currentNamespace = camelNamespaceService().getCurrentNamespace();
                if (!currentNamespace.equals(messageNamespace)) {
                    String errorMessage = String.format("Message received with invalid namespace, expected '%s' but received '%s'", currentNamespace, messageNamespace);
                    LOGGER.error(errorMessage);
                    if (throwExceptionForNamespaceMismatch()) {
                        RuntimeException exception = new InvalidNamespaceException(errorMessage);
                        exchange.setException(exception);
                        throw exception;
                    }
                }
            }

            //and we need to ensure that any messages that could potentially go out to pubsub have a namespace configured
            try {
                addNamespace().process(exchange);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onExchangeDone(Route route, Exchange exchange) {}
    }
}

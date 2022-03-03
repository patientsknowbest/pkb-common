package com.pkb.common.testcontrol.route;

import ch.qos.logback.classic.Level;
import com.pkb.common.testcontrol.camel.route.AbstractNamespaceAwareRouteBuilder;
import com.pkb.common.testcontrol.services.PubSubNamespaceService;
import io.pkb.logcapture.LogCaptureExtension;
import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.pubsub.GooglePubsubConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.pkb.common.testcontrol.camel.route.RouteHelpers.NAMESPACE_HEADER_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
// 9fdb9b
class AbstractNamespaceAwareRouteBuilderTest extends ContextTestSupport {

    public static final String INITIAL_NAMESPACE = "initialNamespace";

    @RegisterExtension
    private final LogCaptureExtension logCaptureExtension = LogCaptureExtension.create().forLevel(Level.OFF).forType(AbstractNamespaceAwareRouteBuilder.class);

    @Mock
    public PubSubNamespaceService service;

    @BeforeEach
    void initialise() {
        when(service.getCurrentNamespace()).thenReturn(INITIAL_NAMESPACE);
    }

    @Test
    // 67cf1f
    void namespacedRoute_noNamespaceOnIncomingMessage_throwsException(TestInfo info) throws Exception {

        logCaptureExtension.forLevel(Level.ERROR);
        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(0);

        template.sendBody("direct:namespaced", "hello");
        assertTrue(logCaptureExtension.isStatementAtLevel("[ERROR] Message received with invalid namespace, expected 'initialNamespace' but received 'no pubsub attributes'", Level.ERROR));
        assertThat(resultEndpoint.getReceivedCounter(), Matchers.is(0));
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    // e6ee57
    void namespacedRoute_differentNamespaceOnIncomingMessage_throwsException(TestInfo info) throws Exception {
        logCaptureExtension.forLevel(Level.ERROR);
        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(0);

        template.sendBodyAndHeader("direct:namespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, "different"));

        assertTrue(logCaptureExtension.isStatementAtLevel("[ERROR] Message received with invalid namespace, expected 'initialNamespace' but received 'different'", Level.ERROR));
        assertThat(resultEndpoint.getReceivedCounter(), Matchers.is(0));
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void namespacedRoute_sameNamespaceOnIncomingMessage_routeCompletes() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:namespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, INITIAL_NAMESPACE));

        assertThat(resultEndpoint.getReceivedCounter(), Matchers.is(1));
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void nonNamespacedRoute_noNamespaceOnIncomingMessage_routeCompletesAndNamespaceIsAdded() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNotNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.message(0).headers().isEqualTo(Map.of(GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, INITIAL_NAMESPACE)));

        template.sendBody("direct:notnamespaced", "hello");

        assertThat(resultEndpoint.getReceivedCounter(), Matchers.is(1));
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void nonNamespacedRoute_differentNamespaceOnIncomingMessage_routeCompletes() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNotNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:notnamespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, "different"));

        assertThat(resultEndpoint.getReceivedCounter(), Matchers.is(1));
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void nonNamespacedRoute_sameNamespaceOnIncomingMessage_routeCompletes() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNotNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:notnamespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, INITIAL_NAMESPACE));

        assertThat(resultEndpoint.getReceivedCounter(), Matchers.is(1));
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new AbstractNamespaceAwareRouteBuilder() {

            @Override
            public @NotNull PubSubNamespaceService camelNamespaceService() {
                return service;
            }

            @Override
            public boolean throwExceptionForNamespaceMismatch() {
                return true;
            }

            @Override
            protected @NotNull String getNamespaceComponentPrefix() {
                return "direct:namespace";
            }

            @Override
            public void configure() {
                from("direct:namespaced").to("mock:resultNamespaced");

                from("direct:notnamespaced").to("mock:resultNotNamespaced");
            }
        };
    }

}
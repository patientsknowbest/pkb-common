package com.pkb.common.testsupport.route;

import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameJsonAsApproved;
import static com.pkb.common.testsupport.camel.route.RouteHelpers.NAMESPACE_HEADER_ATTRIBUTE;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.pubsub.GooglePubsubConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.github.karsaig.approvalcrest.jupiter.MatcherAssert;

import com.pkb.common.testsupport.camel.route.AbstractNamespaceAwareRouteBuilder;
import com.pkb.common.testsupport.services.PubSubNamespaceService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class AbstractNamespaceAwareRouteBuilderTest extends ContextTestSupport {

    public static final String INITIAL_NAMESPACE = "initialNamespace";

    @Mock
    public PubSubNamespaceService service;

    @BeforeEach
    void initialise() {
        when(service.getCurrentNamespace()).thenReturn(INITIAL_NAMESPACE);
    }

    @Test
    void namespacedRoute_noNamespaceOnIncomingMessage_throwsException(TestInfo info) throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(0);

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> template.sendBody("direct:namespaced", "hello"));
        MatcherAssert.assertThat(runtimeException.getCause(), sameJsonAsApproved(info));

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void namespacedRoute_differentNamespaceOnIncomingMessage_throwsException(TestInfo info) throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(0);

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class,
                () -> template.sendBodyAndHeader("direct:namespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, "different")));
        MatcherAssert.assertThat(runtimeException.getCause(), sameJsonAsApproved(info));

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void namespacedRoute_sameNamespaceOnIncomingMessage_routeCompletes() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:namespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, INITIAL_NAMESPACE));

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void nonNamespacedRoute_noNamespaceOnIncomingMessage_routeCompletesAndNamespaceIsAdded() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNotNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.message(0).headers().isEqualTo(Map.of(GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, INITIAL_NAMESPACE)));

        template.sendBody("direct:notnamespaced", "hello");

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void nonNamespacedRoute_differentNamespaceOnIncomingMessage_routeCompletes() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNotNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:notnamespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, "different"));

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void nonNamespacedRoute_sameNamespaceOnIncomingMessage_routeCompletes() throws Exception {

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:resultNotNamespaced", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:notnamespaced", "hello", GooglePubsubConstants.ATTRIBUTES, Map.of(NAMESPACE_HEADER_ATTRIBUTE, INITIAL_NAMESPACE));

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
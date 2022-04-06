package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.camel.route.AbstractTestControlCamelRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.impl.engine.AbstractCamelContext;
import org.apache.camel.spi.RouteStartupOrder;
import org.apache.camel.util.function.ThrowingConsumer;

import java.util.Comparator;

import static com.pkb.common.testcontrol.client.TestControl.IO_PKB_TESTCONTROL_PREFIX;

public class DefaultPubSubNamespaceService implements PubSubNamespaceService {
    private final CamelContext context;
    
    private String currentNamespace = "defaultNS";

    public DefaultPubSubNamespaceService(CamelContext context) {
        this.context = context;
    }

    @Override
    public void setCurrentNamespace(String currentNamespace) {
        // We want to ensure that all _currently_ processing messages are stopped before we change the namespace.
        // This ensures that any unprocessed messages are ignored and don't cause havoc by running while we try to 
        // reset the application internal state.
        applyToRoutesExceptTestControl(route -> context.getRouteController().suspendRoute(route.getRouteId()), true);
        this.currentNamespace = currentNamespace;
        applyToRoutesExceptTestControl(route -> context.getRouteController().resumeRoute(route.getRouteId()), false);
    }

    private void applyToRoutesExceptTestControl(ThrowingConsumer<Route, Exception> routeConsumer, boolean reverseOrder) {
        // This reproduces a bit of logic inside camel; but adding filtering so we can startup and shutdown 
        // non-test-control routes only.
        var comp = Comparator.comparingInt(RouteStartupOrder::getStartupOrder);
        if (reverseOrder) {
            comp = comp.reversed();
        }
        ((AbstractCamelContext)context).getRouteStartupOrder()
                .stream()
                .sorted(comp)
                .map(RouteStartupOrder::getRoute)
                .filter(route -> !route.getProperties().containsKey(AbstractTestControlCamelRouteBuilder.ROUTE_PROPERTY_IS_TEST_CONTROL))
                .filter(route -> !route.getEndpoint().getEndpointUri().contains(IO_PKB_TESTCONTROL_PREFIX)) //DIRECT routes from rest are hard to add properties to...
                .forEach(route -> {
                    try {
                        routeConsumer.accept(route);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public String getCurrentNamespace() {
        return currentNamespace;
    }
}

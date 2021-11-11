package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.camel.route.AbstractTestControlCamelRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.spi.RouteController;
import org.apache.camel.util.function.ThrowingConsumer;

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
        var routeController = context.getRouteController();
        applyToRoutesExceptTestControl(routeController, route -> routeController.stopRoute(route.getRouteId()));
        this.currentNamespace = currentNamespace;
        applyToRoutesExceptTestControl(routeController, route -> routeController.startRoute(route.getRouteId()));
    }

    private void applyToRoutesExceptTestControl(RouteController routeController, ThrowingConsumer<Route, Exception> routeConsumer) {
        routeController.getControlledRoutes()
                .stream()
                .filter(route -> !route.getProperties().containsKey(AbstractTestControlCamelRouteBuilder.ROUTE_PROPERTY_IS_TEST_CONTROL))
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

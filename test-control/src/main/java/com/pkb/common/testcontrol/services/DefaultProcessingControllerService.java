package com.pkb.common.testcontrol.services;

import com.pkb.common.testcontrol.camel.route.AbstractTestControlCamelRouteBuilder;
import com.pkb.common.testcontrol.message.ResumeProcessingRequest;
import com.pkb.common.testcontrol.message.SuspendProcessingRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.impl.engine.AbstractCamelContext;
import org.apache.camel.spi.RouteStartupOrder;
import org.apache.camel.util.function.ThrowingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

import static com.pkb.common.testcontrol.client.TestControl.IO_PKB_TESTCONTROL_PREFIX;

public class DefaultProcessingControllerService implements ProcessingControllerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final CamelContext context;

    public DefaultProcessingControllerService(CamelContext context) {
        this.context = context;
    }

    @Override
    public void process(SuspendProcessingRequest message) {
        LOGGER.info("SuspendProcessingRequest.process message received");
        applyToRoutesExceptTestControl(route -> context.getRouteController().suspendRoute(route.getRouteId()), true);
        LOGGER.info("SuspendProcessingRequest.process done.");
    }

    @Override
    public void process(ResumeProcessingRequest message) {
        LOGGER.info("ResumeProcessingRequest.process message received");
        applyToRoutesExceptTestControl(route -> context.getRouteController().resumeRoute(route.getRouteId()), false);
        LOGGER.info("ResumeProcessingRequest.process done.");
    }

    private void applyToRoutesExceptTestControl(ThrowingConsumer<Route, Exception> routeConsumer, boolean reverseOrder) {
        // This reproduces a bit of logic inside camel; but adding filtering so we can startup and shutdown
        // non-test-control routes only.
        var comp = Comparator.comparingInt(RouteStartupOrder::getStartupOrder);
        if (reverseOrder) {
            comp = comp.reversed();
        }
        ((AbstractCamelContext) context).getRouteStartupOrder()
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
}

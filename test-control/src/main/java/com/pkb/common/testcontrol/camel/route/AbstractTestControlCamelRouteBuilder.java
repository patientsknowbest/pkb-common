package com.pkb.common.testcontrol.camel.route;

import com.pkb.common.testcontrol.config.ITestControlServiceConfig;
import com.pkb.common.testcontrol.message.ClearInternalStateRequest;
import com.pkb.common.testcontrol.message.ClearStorageRequest;
import com.pkb.common.testcontrol.message.DetailedLoggingRequest;
import com.pkb.common.testcontrol.message.FixTimeRequest;
import com.pkb.common.testcontrol.message.ImmutableStartup;
import com.pkb.common.testcontrol.message.InjectConfigRequest;
import com.pkb.common.testcontrol.message.LogTestNameRequest;
import com.pkb.common.testcontrol.message.MoveTimeRequest;
import com.pkb.common.testcontrol.message.NamespaceChangeRequest;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import java.net.URL;

import static com.pkb.common.testcontrol.client.TestControl.IO_PKB_TESTCONTROL_PREFIX;

/**
 * An instance of this needs to be injected into the camel context, either manually or with the relevant
 * injection framework annotation. Then the camel application will configure the endpoints and routes.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractTestControlCamelRouteBuilder extends RouteBuilder {
    public static final String ROUTE_PROPERTY_IS_TEST_CONTROL = "io.pkb.route.property.istestcontrol";

    /**
     * Encapsulates the app-specific config required for these routes
     */
    public abstract ITestControlServiceConfig config();

    /**
     * Basics: https://camel.apache.org/manual/latest/java-dsl.html
     * Makes use of the Simple Expression Language: https://camel.apache.org/components/latest/languages/simple-language.html#top
     */
    @Override
    public void configure() throws Exception {
        if (config().getEnableTestControlEndpoint()) {
            // Configure netty-http with Jackson, listening on specified port 
            getContext().getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
            getContext().getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");
            var selfUrl = new URL(config().getApplicationTestControlCallbackURL());

            restConfiguration().host("0.0.0.0").port(selfUrl.getPort()).component("netty-http").bindingMode(RestBindingMode.json);
            
            if (config().getEnableTestControlRegistration()) {
                var testControlUrl = new URL(config().getTestControlUrl());

                // Maybe replace this with a proper service discovery system later.
                // https://camel.apache.org/components/3.12.x/timer-component.html#_firing_as_soon_as_possible
                // https://camel.apache.org/components/3.12.x/timer-component.html#_endpoint_query_option_synchronous
                from("timer:startup?delay=-1&repeatCount=1&synchronous=true")
                        .routeId("startupMessage")
                        .routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString())
                        .setBody(constant(ImmutableStartup.builder()
                                .name(config().getApplicationName())
                                .callback(config().getApplicationTestControlCallbackURL())
                                .build()))
                        .log(config().getApplicationName() + ": sending startup msg")
                        // Add a redelivery attempt, and die if we couldn't start up.
                        .errorHandler(defaultErrorHandler().maximumRedeliveries(3).redeliveryDelay(100).onPrepareFailure(this::die))
                        .to("rest://put:/register?host=" + testControlUrl.getHost() + ":" + testControlUrl.getPort());
            }
            
            // Routes for test control requests
            rest()
                .put(IO_PKB_TESTCONTROL_PREFIX + "setNamespace").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "setNamespace")
                .put(IO_PKB_TESTCONTROL_PREFIX + "setFixedTimestamp").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "setFixedTimestamp")
                .put(IO_PKB_TESTCONTROL_PREFIX + "moveTime").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "moveTime")
                .put(IO_PKB_TESTCONTROL_PREFIX + "injectConfig").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "injectConfig")
                .put(IO_PKB_TESTCONTROL_PREFIX + "clearInternalState").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "clearInternalState")
                .put(IO_PKB_TESTCONTROL_PREFIX + "clearStorage").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "clearStorage")
                .put(IO_PKB_TESTCONTROL_PREFIX + "logTestName").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "logTestName")
                .put(IO_PKB_TESTCONTROL_PREFIX + "toggleDetailedLogging").to("direct:" + IO_PKB_TESTCONTROL_PREFIX + "toggleDetailedLogging");

            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "setNamespace").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "setNamespace");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "setFixedTimestamp").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "setFixedTimestamp");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "moveTime").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "moveTime");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "injectConfig").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "injectConfig");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "clearInternalState").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "clearInternalState");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "clearStorage").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "clearStorage");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "logTestName").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "logTestName");
            from("direct:" + IO_PKB_TESTCONTROL_PREFIX + "toggleDetailedLogging").routeProperty(ROUTE_PROPERTY_IS_TEST_CONTROL, Boolean.TRUE.toString()).bean(this, "toggleDetailedLogging");
        }
    }
    
    public void die(Exchange ignored) {
        log.error("Failed to register application startup, dying now!!!");
        System.exit(1);
    }

    /**
     * Camel will automatically transform empty string to a 204 response.
     */
    public String setNamespace(NamespaceChangeRequest ncr) {
        config().getNamespaceService().setCurrentNamespace(ncr.newNamespace());
        return "";
    }
    public String setFixedTimestamp(FixTimeRequest request) {
        config().getSetFixedTimestampService().process(request);
        return "";
    }
    public String moveTime(MoveTimeRequest request) {
        config().getMoveTimeService().process(request);
        return "";
    }
    public String injectConfig(InjectConfigRequest request) {
        config().getInjectConfigValueService().process(request);
        return "";
    }
    public String clearInternalState(ClearInternalStateRequest clearInternalStateRequest) {
        config().getClearTestStatesService().process(clearInternalStateRequest);
        return "";
    }
    public String clearStorage(ClearStorageRequest clearStorageRequest) {
        config().getClearStorageService().ifPresent(it -> it.process(clearStorageRequest));
        return "";
    }
    public String logTestName(LogTestNameRequest request) {
        config().getLogTestNameService().process(request);
        return "";
    }
    public String toggleDetailedLogging(DetailedLoggingRequest request) {
        config().getToggleDetailedLoggingService().process(request);
        return "";
    }
}

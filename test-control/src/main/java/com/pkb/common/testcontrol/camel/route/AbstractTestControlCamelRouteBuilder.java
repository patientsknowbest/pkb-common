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
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import java.net.URL;

/**
 * An instance of this needs to be injected into the camel context, either manually or with the relevant
 * injection framework annotation. Then the camel application will configure the endpoints and routes.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractTestControlCamelRouteBuilder extends RouteBuilder {
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
            var testControlUrl = new URL(config().getTestControlUrl());
            
            restConfiguration().host("0.0.0.0").port(selfUrl.getPort()).component("netty-http").bindingMode(RestBindingMode.json);
            
            if (config().getEnableTestControlRegistration()) {
                // Maybe replace this with a proper service discovery system later.
                from("timer:startup?repeatCount=1")
                        .routeId("startupMessage")
                        .setBody(constant(ImmutableStartup.builder()
                                .name(config().getApplicationName())
                                .callback(config().getApplicationTestControlCallbackURL())
                                .build()))
                        .log(config().getApplicationName() + ": sending startup msg")
                        .to("rest://put:/register?host=" + testControlUrl.getHost() + ":" + testControlUrl.getPort());
            }
            
            // Routes for test control requests
            rest()
                    .put("setNamespace").route().bean(this, "setNamespace").endRest()
                    .put("setFixedTimestamp").route().bean(this, "setFixedTimestamp").endRest()
                    .put("moveTime").route().bean(this, "moveTime").endRest()
                    .put("injectConfig").route().bean(this, "injectConfig").endRest()
                    .put("clearInternalState").route().bean(this, "clearInternalState").endRest()
                    .put("clearStorage").route().bean(this, "clearStorage").endRest()
                    .put("logTestName").route().bean(this, "logTestName").endRest()
                    .put("toggleDetailedLogging").route().bean(this, "toggleDetailedLogging").endRest();
        }
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

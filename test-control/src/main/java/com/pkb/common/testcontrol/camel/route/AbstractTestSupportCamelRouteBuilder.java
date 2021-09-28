package com.pkb.common.testcontrol.camel.route;

import com.pkb.common.testcontrol.config.ITestControlServiceConfig;
import com.pkb.common.testcontrol.message.ClearStatesRequest;
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
@SuppressWarnings("WeakerAccess")
public abstract class AbstractTestSupportCamelRouteBuilder extends RouteBuilder {
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
        if (config().getShouldParticipateInTestControl()) {
            // Configure netty-http with Jackson, listening on specified port 
            getContext().getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
            getContext().getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");
            var url = new URL(config().getApplicationTestControlCallbackURL());
            restConfiguration().host("0.0.0.0").port(url.getPort()).component("netty-http").bindingMode(RestBindingMode.json);
            
            // Send registration message, maybe replace this with a proper service discovery mechanism later
            from("timer:startup?repeatCount=1")
                    .routeId("startupMessage")
                    .setBody(constant(ImmutableStartup.builder()
                            .name(config().getApplicationName())
                            .callback(config().getApplicationTestControlCallbackURL())
                            .build()))
                    .log(config().getApplicationName() + ": sending startup msg")
                    .to("rest://put:/register?host="+config().getTestControlHost()+":"+config().getTestControlPort());
            
            // Routes for test control requests
            rest()
                    .put("setNamespace").route().bean(this, "setNamespace").endRest()
                    .put("setFixedTimestamp").route().bean(this, "setFixedTimestamp").endRest()
                    .put("moveTime").route().bean(this, "moveTime").endRest()
                    .put("injectConfig").route().bean(this, "injectConfig").endRest()
                    .put("clearStates").route().bean(this, "clearStates").endRest()
                    .put("logTestName").route().bean(this, "logTestName").endRest()
                    .put("toggleDetailedLogging").route().bean(this, "toggleDetailedLogging").endRest()
            ;
        }
    }

    public void setNamespace(NamespaceChangeRequest ncr) {
        config().getNamespaceService().setCurrentNamespace(ncr.newNamespace());
    }
    public void setFixedTimestamp(FixTimeRequest request) {
        config().getSetFixedTimestampService().process(request);
    }
    public void moveTime(MoveTimeRequest request) {
        config().getMoveTimeService().process(request);
    }
    public void injectConfig(InjectConfigRequest request) {
        config().getInjectConfigValueService().process(request);
    }
    public void clearStates(ClearStatesRequest clearStatesRequest) {
        config().getClearTestStatesService().process(clearStatesRequest);
    }
    public void logTestName(LogTestNameRequest request) {
        config().getLogTestNameService().process(request);
    }
    public void toggleDetailedLogging(DetailedLoggingRequest request) {
        config().getToggleDetailedLoggingService().process(request);
    }
}

package com.pkb.common;

import static io.reactivex.observers.BaseTestConsumer.TestWaitStrategy.SLEEP_100MS;

import java.lang.invoke.MethodHandles;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkb.unit.Bus;
import com.pkb.unit.Filters;
import com.pkb.unit.State;
import com.pkb.unit.message.payload.Transition;

import io.reactivex.observers.TestObserver;

/**
 * A servlet to allow changing the pulsar namespace at runtime.
 */
public class PulsarNamespaceChangeTestSupportServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Bus bus;
    private MutableConfig config;

    @Inject
    public PulsarNamespaceChangeTestSupportServlet(Bus bus,
                                                   MutableConfig config) {
        this.bus = bus;
        this.config = config;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {

        TestObserver<Transition> restartObserver = Filters.payloads(bus.events(), Transition.class)
                .filter(transition ->
                        transition.unitId().equals(DefaultPulsarClient.UNIT_ID)
                        && transition.current() == State.STARTED)
                .test();
        String configValue = req.getParameter("pulsarNamespace");
        LOGGER.info("Setting Pulsar namespace to " + configValue);
        config.setConfig(PulsarClient.PULSAR_NAMESPACE_CONFIG_KEY, configValue);
        // Observe the pulsar client restarting before returning
        restartObserver.awaitCount(1, SLEEP_100MS, 10000);
        if (restartObserver.isTimeout()) {
            throw new RuntimeException("Timed out waiting for pulsar client restart!!!");
        }
        LOGGER.info("Set Pulsar namespace to " + configValue);
    }
}

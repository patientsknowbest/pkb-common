package com.pkb.common.testsupport;

import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.pulsar.IPulsarFactory;
import com.pkb.pulsar.payload.TestControlRequest;
import com.pkb.pulsar.payload.TestControlResponse;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClientException;

import static com.pkb.pulsar.PulsarConstants.TEST_CONTROL_RESPONSE;

public class TestSupportAgent extends AbstractTestSupportAgent {

    public TestSupportAgent(String serviceName, boolean registerStartup, boolean startListener, IPulsarFactory pulsarFactory, DateTimeService dateTimeService, ConfigStorage configStorage) {
        super(serviceName, registerStartup, startListener, pulsarFactory, dateTimeService, configStorage);
    }

    @Override
    protected MessageListener<TestControlRequest> getTestControlRequestService() throws PulsarClientException  {
        return new TestControlRequestService(
                pulsarFactoryWrapper.createTestControlProducer(TEST_CONTROL_RESPONSE, TestControlResponse.class),
                serviceName,
                new PulsarNamespaceChangeService(this.pulsarFactoryWrapper),
                new SetFixedTimestampService(dateTimeService),
                new InjectConfigValueService(configStorage),
                new ClearTestStatesService(dateTimeService, configStorage),
                new LogTestNameService(),
                new ToggleDetailedLoggingService()
                );
    }
}

package com.pkb.common.testcontrol.config;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.common.testcontrol.services.ClearTestStatesService;
import com.pkb.common.testcontrol.services.DefaultClearTestStatesService;
import com.pkb.common.testcontrol.services.DefaultInjectConfigValueService;
import com.pkb.common.testcontrol.services.DefaultLogTestNameService;
import com.pkb.common.testcontrol.services.DefaultMoveTimeService;
import com.pkb.common.testcontrol.services.DefaultSetFixedTimestampService;
import com.pkb.common.testcontrol.services.DefaultToggleDetailedLoggingService;
import com.pkb.common.testcontrol.services.InjectConfigValueService;
import com.pkb.common.testcontrol.services.LogTestNameService;
import com.pkb.common.testcontrol.services.MoveTimeService;
import com.pkb.common.testcontrol.services.PubSubNamespaceService;
import com.pkb.common.testcontrol.services.SetFixedTimestampService;
import com.pkb.common.testcontrol.services.ToggleDetailedLoggingService;

import java.util.Set;

/**
 * Configuration required to handle test control requests
 */
public class TestControlServiceConfig implements ITestControlServiceConfig {
    private final boolean shouldParticipateInTestControl;
    private final String applicationName;
    private final String applicationTestControlCallbackURL;
    private final String testSupportHost;
    private long testSupportPort;
    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;
    private final Set<ClearableInternalState> clearables;
    private final DetailLoggingProvider testLoggingService;
    private final PubSubNamespaceService namespaceService;
    private final SetFixedTimestampService setFixedTimestampService;
    private final MoveTimeService moveTimeService;
    private final InjectConfigValueService injectConfigValueService;
    private final ClearTestStatesService clearTestStatesService;
    private final LogTestNameService logTestNameService;
    private final ToggleDetailedLoggingService toggleDetailedLoggingService;
    

    public TestControlServiceConfig(boolean shouldParticipateInTestControl, 
                                    String applicationName,
                                    String applicationTestControlCallbackURL,
                                    String testSupportHost,
                                    long testSupportPort,
                                    DateTimeService dateTimeService,
                                    ConfigStorage configStorage,
                                    Set<ClearableInternalState> clearables,
                                    DetailLoggingProvider testLoggingService,
                                    PubSubNamespaceService namespaceService) {
        this.applicationName = applicationName;
        this.applicationTestControlCallbackURL = applicationTestControlCallbackURL;
        this.testSupportHost = testSupportHost;
        this.testSupportPort = testSupportPort;
        this.shouldParticipateInTestControl = shouldParticipateInTestControl;
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
        this.clearables = clearables;
        this.testLoggingService = testLoggingService;
        this.namespaceService = namespaceService;
        this.setFixedTimestampService = new DefaultSetFixedTimestampService(dateTimeService);
        this.moveTimeService = new DefaultMoveTimeService(dateTimeService);
        this.injectConfigValueService = new DefaultInjectConfigValueService(configStorage);
        this.clearTestStatesService = new DefaultClearTestStatesService(dateTimeService, configStorage, clearables);
        this.toggleDetailedLoggingService = new DefaultToggleDetailedLoggingService(testLoggingService);
        this.logTestNameService = new DefaultLogTestNameService();
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String getApplicationTestControlCallbackURL() {
        return applicationTestControlCallbackURL;
    }

    @Override
    public String getTestControlHost() {
        return testSupportHost;
    }

    @Override
    public long getTestControlPort() {
        return testSupportPort;
    }

    @Override
    public boolean getShouldParticipateInTestControl() {
        return shouldParticipateInTestControl;
    }

    @Override
    public DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    @Override
    public ConfigStorage getConfigStorage() {
        return configStorage;
    }
    
    @Override
    public Set<ClearableInternalState> getClearables() {
        return clearables;
    }

    @Override
    public DetailLoggingProvider getTestLoggingService() {
        return testLoggingService;
    }

    @Override
    public PubSubNamespaceService getNamespaceService() {
        return namespaceService;
    }


    @Override
    public SetFixedTimestampService getSetFixedTimestampService() {
        return setFixedTimestampService;
    }

    @Override
    public MoveTimeService getMoveTimeService() {
        return moveTimeService;
    }

    @Override
    public InjectConfigValueService getInjectConfigValueService() {
        return injectConfigValueService;
    }

    @Override
    public ClearTestStatesService getClearTestStatesService() {
        return clearTestStatesService;
    }

    @Override
    public LogTestNameService getLogTestNameService() {
        return logTestNameService;
    }

    @Override
    public ToggleDetailedLoggingService getToggleDetailedLoggingService() {
        return toggleDetailedLoggingService;
    }
}

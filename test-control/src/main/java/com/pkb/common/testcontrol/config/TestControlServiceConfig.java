package com.pkb.common.testcontrol.config;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.testcontrol.services.ClearStorageService;
import com.pkb.common.testcontrol.services.ProcessingControllerService;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.common.testcontrol.services.ClearInternalStateService;
import com.pkb.common.testcontrol.services.DefaultClearInternalStateService;
import com.pkb.common.testcontrol.services.DefaultInjectConfigValueService;
import com.pkb.common.testcontrol.services.DefaultLogTestNameService;
import com.pkb.common.testcontrol.services.DefaultMoveTimeService;
import com.pkb.common.testcontrol.services.DefaultSetFixedTimestampService;
import com.pkb.common.testcontrol.services.DefaultToggleDetailedLoggingService;
import com.pkb.common.testcontrol.services.InjectConfigValueService;
import com.pkb.common.testcontrol.services.LogTestNameService;
import com.pkb.common.testcontrol.services.MoveTimeService;
import com.pkb.common.testcontrol.services.SetFixedTimestampService;
import com.pkb.common.testcontrol.services.ToggleDetailedLoggingService;

import java.util.Optional;
import java.util.Set;

/**
 * Configuration required to handle test control requests
 */
public class TestControlServiceConfig implements ITestControlServiceConfig {
    private final boolean enableTestControlEndpoint;
    private final boolean enableTestControlRegistration;
    private final String applicationName;
    private final String applicationTestControlCallbackURL;
    private final String testSupportHost;
    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;
    private final Set<ClearableInternalState> clearables;
    private final DetailLoggingProvider testLoggingService;
    private final SetFixedTimestampService setFixedTimestampService;
    private final MoveTimeService moveTimeService;
    private final InjectConfigValueService injectConfigValueService;
    private final ClearInternalStateService clearInternalStateService;
    private final LogTestNameService logTestNameService;
    private final ToggleDetailedLoggingService toggleDetailedLoggingService;
    private final ProcessingControllerService processingControllerService;
    private final Optional<ClearStorageService> clearStorageService;
    

    public TestControlServiceConfig(boolean enableTestControlEndpoint,
                                    boolean enableTestControlRegistration,
                                    String applicationName,
                                    String applicationTestControlCallbackURL,
                                    String testSupportHost,
                                    DateTimeService dateTimeService,
                                    ConfigStorage configStorage,
                                    Set<ClearableInternalState> clearables,
                                    DetailLoggingProvider testLoggingService,
                                    ProcessingControllerService processingControllerService,
                                    Optional<ClearStorageService> clearStorageService) {
        this.applicationName = applicationName;
        this.applicationTestControlCallbackURL = applicationTestControlCallbackURL;
        this.testSupportHost = testSupportHost;
        this.enableTestControlEndpoint = enableTestControlEndpoint;
        this.enableTestControlRegistration = enableTestControlRegistration;
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
        this.clearables = clearables;
        this.testLoggingService = testLoggingService;
        this.setFixedTimestampService = new DefaultSetFixedTimestampService(dateTimeService);
        this.moveTimeService = new DefaultMoveTimeService(dateTimeService);
        this.injectConfigValueService = new DefaultInjectConfigValueService(configStorage);
        this.clearStorageService = clearStorageService;
        this.clearInternalStateService = new DefaultClearInternalStateService(dateTimeService, configStorage, clearables);
        this.toggleDetailedLoggingService = new DefaultToggleDetailedLoggingService(testLoggingService);
        this.processingControllerService = processingControllerService;
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
    public String getTestControlUrl() {
        return testSupportHost;
    }
    
    @Override
    public boolean getEnableTestControlEndpoint() {
        return enableTestControlEndpoint;
    }

    @Override
    public boolean getEnableTestControlRegistration() {
        return enableTestControlRegistration;
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
    public ClearInternalStateService getClearTestStatesService() {
        return clearInternalStateService;
    }

    @Override
    public Optional<ClearStorageService> getClearStorageService() {
        return clearStorageService;
    }

    @Override
    public LogTestNameService getLogTestNameService() {
        return logTestNameService;
    }

    @Override
    public ProcessingControllerService getProcessingControllerService() {
        return processingControllerService;
    }

    @Override
    public ToggleDetailedLoggingService getToggleDetailedLoggingService() {
        return toggleDetailedLoggingService;
    }
}

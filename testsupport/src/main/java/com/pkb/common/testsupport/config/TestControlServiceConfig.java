package com.pkb.common.testsupport.config;

import java.util.Optional;
import java.util.Set;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.BaseConfig;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.common.testsupport.services.ClearTestStatesService;
import com.pkb.common.testsupport.services.InjectConfigValueService;
import com.pkb.common.testsupport.services.LogTestNameService;
import com.pkb.common.testsupport.services.MoveTimeService;
import com.pkb.common.testsupport.services.PubSubNamespaceService;
import com.pkb.common.testsupport.services.SetFixedTimestampService;
import com.pkb.common.testsupport.services.ToggleDetailedLoggingService;

/**
 * Configuration required to handle test control requests
 */
public class TestControlServiceConfig implements ITestControlServiceConfig {

    private final String applicationName;
    private final String project;
    private final Optional<String> emulatorEndpoint;
    private final boolean shouldRegisterStartup;
    private final boolean shouldStartListener;
    private final DateTimeService dateTimeService;
    private final ConfigStorage configStorage;
    private final BaseConfig baseConfig;
    private final Set<ClearableInternalState> clearables;
    private final DetailLoggingProvider testLoggingService;
    private final PubSubNamespaceService namespaceService;

    private final SetFixedTimestampService setFixedTimestampService;
    private final MoveTimeService moveTimeService;
    private final InjectConfigValueService injectConfigValueService;
    private final ClearTestStatesService clearTestStatesService;
    private final LogTestNameService logTestNameService;
    private final ToggleDetailedLoggingService toggleDetailedLoggingService;


    public TestControlServiceConfig(String applicationName,
                                    String project,
                                    Optional<String> emulatorEndpoint,
                                    boolean shouldRegisterStartup,
                                    boolean shouldStartListener,
                                    DateTimeService dateTimeService,
                                    ConfigStorage configStorage,
                                    Set<ClearableInternalState> clearables,
                                    DetailLoggingProvider testLoggingService,
                                    BaseConfig baseConfig,
                                    PubSubNamespaceService namespaceService) {

        this.applicationName = applicationName;
        this.project = project;
        this.emulatorEndpoint = emulatorEndpoint;
        this.shouldRegisterStartup = shouldRegisterStartup;
        this.shouldStartListener = shouldStartListener;
        this.dateTimeService = dateTimeService;
        this.configStorage = configStorage;
        this.clearables = clearables;
        this.testLoggingService = testLoggingService;
        this.baseConfig = baseConfig;
        this.namespaceService = namespaceService;

        this.setFixedTimestampService = new SetFixedTimestampService(dateTimeService);
        this.moveTimeService = new MoveTimeService(dateTimeService);
        this.injectConfigValueService = new InjectConfigValueService(configStorage);
        this.clearTestStatesService = new ClearTestStatesService(dateTimeService, configStorage, clearables);
        this.logTestNameService = new LogTestNameService(baseConfig);
        this.toggleDetailedLoggingService = new ToggleDetailedLoggingService(baseConfig, testLoggingService);
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public Optional<String> getEmulatorEndpoint() {
        return emulatorEndpoint;
    }

    @Override
    public boolean getShouldRegisterStartup() {
        return shouldRegisterStartup;
    }

    @Override
    public boolean getShouldStartListener() {
        return shouldStartListener;
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
    public BaseConfig getBaseConfig() {
        return baseConfig;
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

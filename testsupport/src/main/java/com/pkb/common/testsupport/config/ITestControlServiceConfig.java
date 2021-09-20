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

public interface ITestControlServiceConfig {
    Optional<String> getEmulatorEndpoint();

    String getProject();

    SetFixedTimestampService getSetFixedTimestampService();

    MoveTimeService getMoveTimeService();

    InjectConfigValueService getInjectConfigValueService();

    ClearTestStatesService getClearTestStatesService();

    LogTestNameService getLogTestNameService();

    ToggleDetailedLoggingService getToggleDetailedLoggingService();

    String getApplicationName();

    boolean getShouldRegisterStartup();

    boolean getShouldStartListener();

    DateTimeService getDateTimeService();

    ConfigStorage getConfigStorage();

    BaseConfig getBaseConfig();

    Set<ClearableInternalState> getClearables();

    DetailLoggingProvider getTestLoggingService();

    PubSubNamespaceService getNamespaceService();
}

package com.pkb.common.testsupport.config;

import com.pkb.common.ClearableInternalState;
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

import java.util.Optional;
import java.util.Set;

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

    Set<ClearableInternalState> getClearables();

    DetailLoggingProvider getTestLoggingService();

    PubSubNamespaceService getNamespaceService();

    default boolean testControlTimeoutEnabled() {
        return true;
    }

    default long testControlTimeoutMillis() {
        return 30000;
    }

    default long testControlHandlerDelayMillis() {
        return 0;
    }

    default int testControlHandlerRequestVolumeThreshold() {
        return 1000;
    }

    default int testControlHandlerFailureRatio() {
        return 100;
    }

    default int testControlHandlerSuccessThreshold() {
        return 100;
    }
}

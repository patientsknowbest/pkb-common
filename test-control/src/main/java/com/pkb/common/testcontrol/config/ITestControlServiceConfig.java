package com.pkb.common.testcontrol.config;

import com.pkb.common.ClearableInternalState;
import com.pkb.common.config.ConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.testcontrol.services.ClearStorageService;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.common.testcontrol.services.ClearInternalStateService;
import com.pkb.common.testcontrol.services.InjectConfigValueService;
import com.pkb.common.testcontrol.services.LogTestNameService;
import com.pkb.common.testcontrol.services.MoveTimeService;
import com.pkb.common.testcontrol.services.PubSubNamespaceService;
import com.pkb.common.testcontrol.services.SetFixedTimestampService;
import com.pkb.common.testcontrol.services.ToggleDetailedLoggingService;

import java.util.Optional;
import java.util.Set;

/**
 * Platform-agnostic interface for test control configuration.
 */
public interface ITestControlServiceConfig {
    /**
     * Typically should be enabled for e2e or integration testing, and disabled in production.
     */
    boolean getEnableTestControlEndpoint();

    /**
     * Enabled for e2e, disabled for some integration-testing configurations where 
     * only a single application is under test.
     */
    boolean getEnableTestControlRegistration();
    
    /**
     * Application name must be unique among applications participating in test-control
     */
    String getApplicationName();

    /**
     * The URL which test-control will call out to
     */
    String getApplicationTestControlCallbackURL();

    /**
     * URL for test-control server, we'll register ourselves there.
     */
    String getTestControlUrl();

    //Handles to services that test-control delegates to
    DateTimeService getDateTimeService();
    ConfigStorage getConfigStorage();
    Set<ClearableInternalState> getClearables();
    DetailLoggingProvider getTestLoggingService();
    PubSubNamespaceService getNamespaceService();
    SetFixedTimestampService getSetFixedTimestampService();
    MoveTimeService getMoveTimeService();
    InjectConfigValueService getInjectConfigValueService();
    ClearInternalStateService getClearTestStatesService();
    Optional<ClearStorageService> getClearStorageService();
    LogTestNameService getLogTestNameService();
    ToggleDetailedLoggingService getToggleDetailedLoggingService();
}

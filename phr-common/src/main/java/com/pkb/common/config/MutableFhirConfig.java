package com.pkb.common.config;

/**
 * This config class allows dynamic injection of values at runtime
 * It's designed to be used when the code is in test, for example to flip feature switches at runtime
 * For production code, use {@link ImmutablePhrConfig}
 */
public class MutableFhirConfig extends BaseMutableConfig implements FhirConfig {

    private FhirConfig defaultConfig;

    public MutableFhirConfig(FhirConfig defaultConfig) {
        super();
        this.defaultConfig = defaultConfig;
    }

    @Override
    public boolean isFhirApiExperimental() {
        return getBooleanValue(configMap.get("fhirApiExperimental"))
                .orElseGet(() -> defaultConfig.isFhirApiExperimental());
    }

    @Override
    public int getFhirObservationMaxNumberOfResources() {
        return getIntValue(configMap.get("fhirObsMaxResources"))
                .orElseGet(() -> defaultConfig.getFhirObservationMaxNumberOfResources());
    }

    @Override
    public int getFhirAppointmentMaxNumberOfResources() {
        return getIntValue(configMap.get("fhirApptMaxResources"))
                .orElseGet(() -> defaultConfig.getFhirAppointmentMaxNumberOfResources());
    }

    @Override
    public int getFhirCommunicationMaxNumberOfResources() {
        return getIntValue(configMap.get("fhirCommsMaxResources"))
                .orElseGet(() -> defaultConfig.getFhirCommunicationMaxNumberOfResources());
    }

    @Override
    public int getFhirDiagnosticReportMaxNumberOfResources() {
        return getIntValue(configMap.get("fhirDiagMaxResources"))
                .orElseGet(() -> defaultConfig.getFhirDiagnosticReportMaxNumberOfResources());
    }

    @Override
    public int getFhirEncounterMaxNumberOfResources() {
        return getIntValue(configMap.get("fhirEncounterMaxResources"))
                .orElseGet(() -> defaultConfig.getFhirEncounterMaxNumberOfResources());
    }

    @Override
    public int getFhirDocumentReferenceMaxNumberOfResources() {
        return getIntValue(configMap.get("fhirDocRefMaxResources"))
                .orElseGet(() -> defaultConfig.getFhirDocumentReferenceMaxNumberOfResources());
    }

    @Override
    public boolean isFhirCommunicationResourceEnabled() {
        return getBooleanValue(configMap.get("fhirCommsResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirCommunicationResourceEnabled());
    }

    @Override
    public String getSynertecApiClientId() {
        String synertecApiClientId = configMap.get("synertecApiClientId");
        if (synertecApiClientId != null) {
            return synertecApiClientId;
        }
        return defaultConfig.getSynertecApiClientId();
    }

    @Override
    public int getSlowDocRefQueryAlertThresholdSeconds() {
        return getIntValue(configMap.get("slowDocRefQueryAlertThreshold"))
                .orElseGet(() -> defaultConfig.getSlowDocRefQueryAlertThresholdSeconds());
    }

    @Override
    public boolean isFhirAppointmentResourceEnabled() {
        return getBooleanValue(configMap.get("fhirApptResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirAppointmentResourceEnabled());
    }

    @Override
    public boolean isFhirEncounterResourceEnabled() {
        return getBooleanValue(configMap.get("fhirEncounterResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirEncounterResourceEnabled());
    }

    @Override
    public boolean isFhirPersonResourceEnabled() {
        return getBooleanValue(configMap.get("fhirPersonResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirPersonResourceEnabled());
    }

    @Override
    public boolean isFhirPatientResourceEnabled() {
        return getBooleanValue(configMap.get("fhirPatientResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirPatientResourceEnabled());
    }

    @Override
    public boolean isFhirPractitionerResourceEnabled() {
        return getBooleanValue(configMap.get("fhirPractitionerResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirPractitionerResourceEnabled());
    }

    @Override
    public boolean isFhirOrganizationResourceEnabled() {
        return getBooleanValue(configMap.get("fhirOrganizationResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirOrganizationResourceEnabled());
    }

    @Override
    public boolean isFhirNamingSystemResourceEnabled() {
        return getBooleanValue(configMap.get("fhirNamingSystemResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirNamingSystemResourceEnabled());
    }

    @Override
    public boolean isFhirConsentResourceEnabled() {
        return getBooleanValue(configMap.get("fhirConsentResourceEnabled"))
                .orElseGet(() -> defaultConfig.isFhirConsentResourceEnabled());
    }

    @Override
    public boolean isFhirPurviewOperationEnabled() {
        return getBooleanValue(configMap.get("fhirPurviewOperationEnabled"))
                .orElseGet(() -> defaultConfig.isFhirPurviewOperationEnabled());
    }

    @Override
    public boolean isFhirDiagnosticReportResourceEnabled() {
        return getBooleanValue(configMap.get("fhirDiagReportEnabled"))
                .orElseGet(() -> defaultConfig.isFhirDiagnosticReportResourceEnabled());
    }

    @Override
    public boolean isFhirDocumentReferenceResourceEnabled() {
        return getBooleanValue(configMap.get("fhirDocRefEnabled"))
                .orElseGet(() -> defaultConfig.isFhirDocumentReferenceResourceEnabled());
    }

    @Override
    public boolean isFhirObservationResourceEnabled() {
        return getBooleanValue(configMap.get("fhirObsEnabled"))
                .orElseGet(() -> defaultConfig.isFhirObservationResourceEnabled());
    }

    @Override
    public String getOauth2BaseUrl() {
        String oauth2BaseUrl = configMap.get("oauth2.baseurl");
        if (oauth2BaseUrl != null) {
            return oauth2BaseUrl;
        }
        return defaultConfig.getOauth2BaseUrl();
    }

    @Override
    public String getOauth2PhrClientUsername() {
        String oauth2PhrClientUsername = configMap.get("oauth2.phr.client.username");
        if (oauth2PhrClientUsername != null) {
            return oauth2PhrClientUsername;
        }
        return defaultConfig.getOauth2PhrClientUsername();
    }

    @Override
    public String getOauth2PhrClientPassword() {
        String oauth2PhrClientPassword = configMap.get("oauth2.phr.client.password");
        if (oauth2PhrClientPassword != null) {
            return oauth2PhrClientPassword;
        }
        return defaultConfig.getOauth2PhrClientPassword();
    }

    @Override
    public int getOauth2PhrClientTokenCacheExpireMinutes() {
        return getIntValue(configMap.get("oauth2.phr.client.token.cache.expire.minutes"))
                .orElseGet(() -> defaultConfig.getOauth2PhrClientTokenCacheExpireMinutes());
    }

    @Override
    public String orgNetworkSyncUrl() {
        String orgNetworkSyncUrl = configMap.get("fhir.api.org.network.sync.url");
        if (orgNetworkSyncUrl != null) {
            return orgNetworkSyncUrl;
        }
        return defaultConfig.orgNetworkSyncUrl();
    }

    @Override
    public String uploadedDataSyncUrl() {
        String uploadedDataSyncUrl = configMap.get("fhir.api.uploadeddata.sync.url");
        if (uploadedDataSyncUrl != null) {
            return uploadedDataSyncUrl;
        }
        return defaultConfig.uploadedDataSyncUrl();
    }

    @Override
    public long fhirToStrutsConnectionTimeoutMilliseconds() {
        return getLongValue(configMap.get("fhir.api.to.struts.connectiontimeout.milliseconds"))
                .orElseGet(() -> defaultConfig.fhirToStrutsConnectionTimeoutMilliseconds());
    }

    @Override
    public long fhirToStrutsConnectionWriteTimeoutMilliseconds() {
        return getLongValue(configMap.get("fhir.api.to.struts.write.milliseconds"))
                .orElseGet(() -> defaultConfig.fhirToStrutsConnectionWriteTimeoutMilliseconds());
    }

    @Override
    public long fhirToStrutsReadTimeoutMilliseconds() {
        return getLongValue(configMap.get("fhir.api.to.struts.read.milliseconds"))
                .orElseGet(() -> defaultConfig.fhirToStrutsReadTimeoutMilliseconds());
    }

    @Override
    public String getPulsarServiceURL() {
        String pulsarServiceURL = configMap.get("pulsarServiceURL");
        if (pulsarServiceURL != null) {
            return pulsarServiceURL;
        }
        return defaultConfig.getPulsarServiceURL();
    }

    @Override
    public String getPulsarDefaultNamespce() {
        String pulsarDefaultNamespce = configMap.get("pulsarDefaultNamespce");
        if (pulsarDefaultNamespce != null) {
            return pulsarDefaultNamespce;
        }
        return defaultConfig.getPulsarDefaultNamespce();    }


    @Override
    public boolean isPulsarServiceRegistrationEnabled() {
        return getBooleanValue(configMap.get("pulsarServiceRegistrationEnabled"))
                .orElseGet(() -> defaultConfig.isPulsarServiceRegistrationEnabled());
    }

    @Override
    public boolean isPulsarTestSupportServicesEnabled() {
        return getBooleanValue(configMap.get("pulsarTestSupportServicesEnabled"))
                .orElseGet(() -> defaultConfig.isPulsarTestSupportServicesEnabled());
    }

    @Override
    BaseConfig getDefaultConfig() {
        return defaultConfig;
    }
}

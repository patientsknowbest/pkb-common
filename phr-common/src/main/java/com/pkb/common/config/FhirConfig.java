package com.pkb.common.config;

public interface FhirConfig extends SecondaryBaseConfig {
    boolean isFhirApiExperimental();

    int getFhirObservationMaxNumberOfResources();

    int getFhirAppointmentMaxNumberOfResources();

    int getFhirCommunicationMaxNumberOfResources();

    int getFhirDiagnosticReportMaxNumberOfResources();

    int getFhirEncounterMaxNumberOfResources();

    int getFhirDocumentReferenceMaxNumberOfResources();

    boolean isFhirCommunicationResourceEnabled();

    String getSynertecApiClientId();

    int getSlowDocRefQueryAlertThresholdSeconds();

    boolean isFhirAppointmentResourceEnabled();

    boolean isFhirEncounterResourceEnabled();

    boolean isFhirPersonResourceEnabled();

    boolean isFhirPatientResourceEnabled();

    boolean isFhirPractitionerResourceEnabled();

    boolean isFhirOrganizationResourceEnabled();

    boolean isFhirNamingSystemResourceEnabled();

    boolean isFhirConsentResourceEnabled();

    boolean isFhirPurviewOperationEnabled();

    boolean isFhirDiagnosticReportResourceEnabled();

    boolean isFhirDocumentReferenceResourceEnabled();

    boolean isFhirObservationResourceEnabled();

    String getOauth2BaseUrl();

    String getOauth2PhrClientUsername();

    String getOauth2PhrClientPassword();

    int getOauth2PhrClientTokenCacheExpireMinutes();

    String orgNetworkSyncUrl();

    String uploadedDataSyncUrl();

    long fhirToStrutsConnectionTimeoutMilliseconds();

    long fhirToStrutsConnectionWriteTimeoutMilliseconds();

    long fhirToStrutsReadTimeoutMilliseconds();

    String getPulsarServiceURL();

    String getPulsarDefaultNamespce();

    boolean isPulsarServiceRegistrationEnabled();

    boolean isPulsarTestSupportServicesEnabled();
}

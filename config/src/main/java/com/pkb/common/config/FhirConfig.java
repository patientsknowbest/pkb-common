package com.pkb.common.config;

public interface FhirConfig extends BaseConfig {
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
}

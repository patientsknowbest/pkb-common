package com.pkb.common.config;

/**
 * TODO: This is just a workaround for CDI (it cannot proxy ConfigV2) due to its private constructor which part of another workaround.
 */
public interface Configuration {

    String getBaseURL();

    boolean isFhirApiExperimental();

    int getMenudataQueryBatchSize();

    boolean isFakeDateTimeServiceEnabled();

    boolean isFhirAppointmentResourceEnabled();

    boolean isFhirEncounterResourceEnabled();

    boolean isFhirPersonResourceEnabled();

    boolean isFhirPatientResourceEnabled();

    boolean isFhirPractitionerResourceEnabled();

    boolean isFhirOrganizationResourceEnabled();

    boolean isFhirNamingSystemResourceEnabled();

    boolean isFhirConsentResourceEnabled();

    boolean isFhirPurviewOperationEnabled();

    boolean isFhirApiEnabled();

    boolean isFhirDiagnosticReportResourceEnabled();

    String getVcMaxWarningDate();

    boolean isFhirDocumentReferenceResourceEnabled();

    boolean isFhirObservationResourceEnabled();

    int getFhirObservationMaxNumberOfResources();

    int getFhirAppointmentMaxNumberOfResources();

    int getFhirDiagnosticReportMaxNumberOfResources();

    int getFhirEncounterMaxNumberOfResources();

    int getFhirDocumentReferenceMaxNumberOfResources();

    boolean isExceptionForNullUniqueIdEnabled();

    boolean isExceptionForMissingSourceEnabled();
}

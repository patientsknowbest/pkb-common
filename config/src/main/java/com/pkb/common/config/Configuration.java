package com.pkb.common.config;

/**
 * TODO: This is just a workaround for CDI (it cannot proxy ConfigV2) due to its private constructor which part of another workaround.
 */
public interface Configuration {
    boolean isFhirApiExperimental();

    int getMenudataQueryBatchSize();

    boolean isFhirPatientResourceEnabled();

    boolean isFhirPractitionerResourceEnabled();

    boolean isFhirOrganizationResourceEnabled();

    boolean isFhirNamingSystemResourceEnabled();

    boolean isFhirConsentResourceEnabled();

    boolean isFhirPurviewOperationEnabled();

    boolean isFhirApiEnabled();
}

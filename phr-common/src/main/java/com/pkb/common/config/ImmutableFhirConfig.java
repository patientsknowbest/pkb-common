package com.pkb.common.config;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmutableFhirConfig extends BaseImmutableConfig implements FhirConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ImmutableFhirConfig(RawConfigStorage storage) {
        super(storage);
    }

    @Override
    public boolean isFhirApiExperimental() {
        return storage.getBoolean("fhir.api.experimental", false);
    }

    @Override
    public boolean isFhirAppointmentResourceEnabled() {
        return storage.getBoolean("fhir.api.Appointment.enabled", true);
    }

    @Override
    public boolean isFhirEncounterResourceEnabled() {
        return storage.getBoolean("fhir.api.Encounter.enabled", true);
    }

    @Override
    public boolean isFhirPatientResourceEnabled() {
        return storage.getBoolean("fhir.api.Patient.enabled", true);
    }

    @Override
    public boolean isFhirPractitionerResourceEnabled() {
        return storage.getBoolean("fhir.api.Practitioner.enabled", true);
    }

    @Override
    public boolean isFhirOrganizationResourceEnabled() {
        return storage.getBoolean("fhir.api.Organization.enabled", true);
    }

    @Override
    public boolean isFhirNamingSystemResourceEnabled() {
        return storage.getBoolean("fhir.api.NamingSystem.enabled", true);
    }

    @Override
    public boolean isFhirConsentResourceEnabled() {
        return storage.getBoolean("fhir.api.Consent.enabled", true);
    }

    @Override
    public boolean isFhirPurviewOperationEnabled() {
        return storage.getBoolean("fhir.api.purview.enabled", true);
    }

    @Override
    public boolean isFhirPersonResourceEnabled() {
        return storage.getBoolean("fhir.api.Person.enabled", false);
    }

    @Override
    public boolean isFhirObservationResourceEnabled() {
        return storage.getBoolean("fhir.api.Observation.enabled", true);
    }

    @Override
    public boolean isFhirDiagnosticReportResourceEnabled() {
        return storage.getBoolean("fhir.api.DiagnosticReport.enabled", true);
    }

    @Override
    public boolean isFhirDocumentReferenceResourceEnabled() {
        return storage.getBoolean("fhir.api.DocumentReference.enabled", true);
    }

    @Override
    public int getFhirObservationMaxNumberOfResources() {
        return storage.getInt("fhir.api.Observation.maxNumberOfResources", 60000);
    }

    @Override
    public int getFhirAppointmentMaxNumberOfResources() {
        return storage.getInt("fhir.api.Appointment.maxNumberOfResources", 1000);
    }

    @Override
    public int getFhirCommunicationMaxNumberOfResources() {
        return storage.getInt("fhir.api.Communication.maxNumberOfResources", 1000);
    }

    @Override
    public int getFhirDiagnosticReportMaxNumberOfResources() {
        return storage.getInt("fhir.api.DiagnosticReport.maxNumberOfResources", 1000);
    }

    @Override
    public int getFhirEncounterMaxNumberOfResources() {
        return storage.getInt("fhir.api.Encounter.maxNumberOfResources", 1000);
    }

    @Override
    public int getFhirDocumentReferenceMaxNumberOfResources() {
        return storage.getInt("fhir.api.DocumentReference.maxNumberOfResources", 10000);
    }

    @Override
    public boolean isFhirCommunicationResourceEnabled() {
        return storage.getBoolean("fhir.api.Communication.enabled", true);
    }

    @Override
    public String getSynertecApiClientId() {
        return storage.getString("synertecApiClientId", "synertec-user-test");
    }


    @Override
    public int getSlowDocRefQueryAlertThresholdSeconds() {
        return storage.getInt("slowDocRefQueryAlertThresholdSeconds", 30);
    }

    @Override
    public String getOauth2BaseUrl() {
        return storage.getString("oauth2.baseurl", "http://oauth2-endpoint:8080");
    }

    @Override
    public String getOauth2PhrClientUsername() {
        return storage.getString("oauth2.phr.client.username", "f60604fa-9b40-461b-ae7d-0140d68f2fe4");
    }

    @Override
    public String getOauth2PhrClientPassword() {
        return storage.getString("oauth2.phr.client.password", "pass");
    }

    @Override
    public int getOauth2PhrClientTokenCacheExpireMinutes() {
        return storage.getInt("oauth2.phr.client.token.cache.expire.minutes", 20);
    }

    @Override
    public String orgNetworkSyncUrl() {
        return storage.getString("fhir.api.org.network.sync.url", "http://phr-wf:80/orgNetworkSync.action");
    }

    @Override
    public String uploadedDataSyncUrl() {
        return storage.getString("fhir.api.uploadeddata.sync.url", "http://phr-wf:80/synchUploadedData.action");
    }

    @Override
    public long fhirToStrutsConnectionTimeoutMilliseconds() {
        return storage.getLong("fhir.api.to.struts.connectiontimeout.milliseconds", SECONDS.toMillis(10));
    }

    @Override
    public long fhirToStrutsConnectionWriteTimeoutMilliseconds() {
        return storage.getLong("fhir.api.to.struts.write.milliseconds", SECONDS.toMillis(10));
    }

    @Override
    public long fhirToStrutsReadTimeoutMilliseconds() {
        return storage.getLong("fhir.api.to.struts.read.milliseconds", MINUTES.toMillis(2));
    }

    @Override
    public String getPulsarServiceURL() {
        return storage.getString("pulsarServiceURL", "pulsar://pulsar:6650");
    }

    @Override
    public String getPulsarDefaultNamespce() {
        return storage.getString("pulsarDefaultNamespce", "defaultNS");
    }

    @Override
    public boolean isPulsarServiceRegistrationEnabled() {
        return storage.getBoolean("pulsarServiceRegistrationEnabled", false);
    }

    @Override
    public boolean isPulsarTestSupportServicesEnabled() {
        return storage.getBoolean("pulsarTestSupportServicesEnabled", false);
    }
}

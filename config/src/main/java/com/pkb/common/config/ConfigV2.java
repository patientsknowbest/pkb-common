package com.pkb.common.config;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigV2 implements Configuration {

    private static final class ConfigV2InstanceHolder {
        private static final ConfigV2 INSTANCE = new ConfigV2();
    }

    // FIXME: this is just a workaround to avoid loading config files multiple times.
    /**
     * @deprecated Use CDI or Spring to wire an instance into your service.
     */
    @Deprecated
    public static @NotNull ConfigV2 getInstance() {
        return ConfigV2InstanceHolder.INSTANCE;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RawConfigStorage storage;

    private ConfigV2() {
        this(RawConfigStorage.createDefault());
    }

    public ConfigV2(RawConfigStorage storage) {
        this.storage = storage;
    }

    /**
     * @param protocol
     *            defaults to http
     * @param host
     *            valid value required
     * @param port
     *            defaults to none specified (defaults ports 80/443 if specified will be removed)
     * @return example: protocol://host:port/application, or protocol://host no trailing slash unless included in application parameter
     */
    private String buildCleanUrl(String protocol, String host, String port) {
        if ((protocol == null) || protocol.isEmpty()) {
            protocol = "http";
        }
        protocol = protocol.toLowerCase();

        if ((host == null) || host.isEmpty()) {
            throw new IllegalArgumentException("valid host is required");
        }

        String colonPort = ":" + port;
        if ((port == null) || port.isEmpty()) {
            colonPort = "";
        } else if (port.equals("80") || port.equals("443")) {
            colonPort = "";
        } else if ((port.equals("80") && protocol.equals("https")) ||
                (port.equals("443") && protocol.equals("http"))) {
            throw new IllegalArgumentException("incompatible: protocol " + protocol + " and port " + port);
        }

        return protocol + "://" + host + colonPort;
    }

    @Override
    public String getBaseURL() {

        // parse it so that we can clean it (don't show port if default, etc.)
        URL baseURL = null;

        try {
            baseURL = new URL(storage.getString("baseURL"));
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid URL value for baseURL: " + storage.getString("baseURL"), e);
        }
        int portInt = baseURL.getPort();
        String port = (portInt > 0) ? String.valueOf(portInt) : "";

        if (!baseURL.getPath().isEmpty()) {
            throw new IllegalArgumentException("path not permitted in baseURL: " + baseURL.getPath());
        }

        return buildCleanUrl(
                baseURL.getProtocol(),
                baseURL.getHost(),
                port);
    }

    public String getFromEmailAddress() {
        return storage.getString("fromEmailAddress", "");
    }

    public String getFromEmailName() {
        return storage.getString("fromEmailName", "");
    }

    public int getInboxPageSize() {
        return storage.getInt("inboxPageSize");
    }

    public int getPatientFilesPerPage() {
        return storage.getInt("patientFilesPerPage");
    }

    public int getPatientDiaryEntriesPerPage() {
        return storage.getInt("patientDiaryEntriesPerPage");
    }

    public String getPKBLogoURL() {
        return storage.getString("pkbLogoURL", "");
    }

    public String getTestEmailId() {
        return storage.getString("testEmailId", "");
    }

    public String getHelpPhone() {
        return storage.getString("helpPhoneNumber", "");
    }

    public String getHelpEmailAddress() {
        return storage.getString("helpEmailAddress", "");
    }

    public String getHelpWebSite() {
        return storage.getString("helpWebSite", "");
    }

    /** always ends with / if there's a value (enforced in this method) */
    public String getInstituteLogoBaseDir() {
        String baseUrl = storage.getString("instituteLogoBaseDirectory", "");
        if (isNotBlank(baseUrl) && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }

    private static boolean isNotBlank(String str) {
        return str != null && !str.trim().equals("");
    }

    public String getPHPlanTemplateImageBaseDir() {
        return storage.getString("pHPlanTemplateImageBaseDirectory", "");
    }

    public Path getImageUploadLogoBaseDirectory() {
        return Paths.get(storage.getString("imageUploadLogoBaseDirectory", ""));
    }

    public String getImageUploadPHPlanTemplateImageBaseDirectory() {
        return storage.getString("imageUploadPHPlanTemplateImageBaseDirectory", "");
    }

    public String getSftpBaseDir() {
        return storage.getString("sftpBaseDir", "");
    }

    public String getHelpName() {
        return storage.getString("helpName", "");
    }

    public int getRecipientColumnSize() {
        return storage.getInt("recipientColumnSize", 3);
    }

    public int getThumbnailImageWidth() {
        return storage.getInt("thumbnailImageWidth", 400);
    }

    public int getThumbnailImageHeight() {
        return storage.getInt("thumbnailImageHeight", 400);
    }

    public int getRadiologyReportExtractLength() {
        return storage.getInt("radiologyReportExtractLength", 200);
    }

    public String getThumbnailImageFormat() {
        return storage.getString("thumbnailImageFormat", "png");
    }

    public boolean getSendReminderEmailsEnabled() {
        return storage.getBoolean("sendReminderEmailsEnabled", false);
    }

    public boolean getUnreadMessageReminderEnabled() {
        return storage.getBoolean("sendUnreadMessageReminderEnabled", false);
    }

    public boolean getUnreadDocumentReminderEnabled() {
        return storage.getBoolean("sendUnreadDocumentReminderEnabled", false);
    }

    public String getWithingsOauthKey() {
        return storage.getString("withingsOauthKey", "");
    }

    public String getWithingsOauthSecret() {
        return storage.getString("withingsOauthSecret", "");
    }

    public String getVitaDockOauthKey() {
        return storage.getString("vitaDockOauthKey", "");
    }

    public String getVitaDockOauthSecret() {
        return storage.getString("vitaDockOauthSecret", "");
    }

    public String getSciStoreEndpoint() {
        return storage.getString("sciStoreEndpoint");
    }

    public String getSciStoreUsername() {
        return storage.getString("sciStoreUsername");
    }

    public String getSciStorePassword() {
        return storage.getString("sciStorePassword");
    }

    public String getFakeS3Endpoint() {
        return storage.getString("fakeS3Endpoint");
    }

    public boolean isDefinedFakeS3Endpoint() {
        return storage.containsKey("fakeS3Endpoint");
    }

    public String getFakeS3EndpointPublishedURL() {
        return storage.getString("fakeS3EndpointPublishedURL");
    }

    public int getOrgAdminDashboardPageSize() {
        return storage.getInt("orgAdminDashboardPageSize");
    }

    public int getAccessLogPageSize() {
        return storage.getInt("accessLogPageSize");
    }

    public String getCoreDevicesOrganizationId() {
        return storage.getString("coreDevicesOrganizationId");
    }

    public String getCoreDevicesAccessToken() {
        return storage.getString("coreDevicesAccessToken");
    }

    public int getSciStoreNewPatientLimit() {
        return storage.getInt("integration.scistore.newpatientlimit", 0);
    }

    public int getLatestResultsInitialThreshold() {
        return storage.getInt("latestResultsInitialThreshold");
    }

    public int getHighchartsThreshold() {
        return storage.getInt("highchartsThreshold");
    }

    public boolean getUseUTCInHighcharts() {
        return storage.getBoolean("useUTCInHighcharts", false);
    }

    public boolean getSortEmisCsvs() {
        return storage.getBoolean("integrations.emis.csv.sort", true);
    }

    public String getLabtestsonlineSearchURL() {
        return storage.getString("labtestsonlineSearchURL");
    }

    public String getLabtestsonlineRefURL() {
        return storage.getString("labtestsonlineRefURL");
    }

    public String getHelpTextURL() {
        return storage.getString("helpTextURL");
    }

    public int getFileChunkSizeInBytes() {
        return Integer.parseInt(storage.getString("fileChunkSizeInBytes"));
    }

    public int getDocDeleteBatchSize() {
        return Integer.parseInt(storage.getString("docDeleteBatchSize", "20"));
    }

    public long getPatientBannerErrorTimeoutMillis() {
        return storage.getLong("feature.patientbanner.errorTimeoutMillis", 3000L);
    }

    public int getDocDeleteIntervalInHours() {
        return Integer.parseInt(storage.getString("docDeleteIntervalInHours", "8"));
    }

    public Boolean getPrometheusReportingEnabled() {
        return storage.getBoolean("prometheusReportingEnabled", Boolean.TRUE);
    }

    public String getLibraryUploadDirectory() {
        return storage.getString("libraryUploadBaseDirectory");
    }

    public String getNonCryptoAccessKey() {
        return storage.getString("nonCryptoAccessKey");
    }

    public String getNonCryptoSecretKey() {
        return storage.getString("nonCryptoSecretKey");
    }

    public String getNonCryptoBucket() {
        return storage.getString("nonCryptoBucket");
    }

    public String getProxyHost() {
        return storage.getString("proxyHost");
    }

    public int getProxyPort() {
        return storage.getInt("proxyPort");
    }

    public String getEmisEsDownloadBaseDir() {
        return storage.getString("emisEsDownloadBaseDir");
    }

    public String getEmisEsSftpRemoteDirectory() {
        return storage.getString("emisEsSftpRemoteDirectory");
    }

    public String getEmisEsPgpPrivateKeyFile() {
        return storage.getString("emisEsPgpPrivateKeyFile");
    }

    public String getEmisEsPgpPrivateKeyFilePassphrase() {
        return storage.getString("emisEsPgpPrivateKeyFilePassphrase", "");
    }

    public Boolean isEmisEsNotificationEnabled() {
        return storage.getBoolean("emisEsNotificationEnabled", false);
    }

    public String getEmisEsCsvEncoding() {
        return storage.getString("emisEsCsvEncoding", "UTF-8");
    }

    public String getEmisEsConfigBaseDir() {
        return storage.getString("emisEsConfigBaseDir", "/pkb/emis");
    }

    public Boolean isEmisEsWhiteListEnabled() {
        return storage.getBoolean("emisEsWhiteListEnabled", true);
    }

    public int getEmisFailureGracePeriodHours() {
        return storage.getInt("emisEsFailureGracePeriodHours");
    }

    public Path getEmisEsSftpConfigYamlPath() {
        return Paths.get(storage.getString("emisEsSftpConfigYaml"));
    }

    public Optional<Path> getEmisEsWhitelistYamlPath() {

        Optional<Path> whitelistYamlPath = empty();

        if (storage.getString("emisEsWhitelistYaml") != null) {
            whitelistYamlPath = Optional.of(Paths.get(storage.getString("emisEsWhitelistYaml")));

        }
        return whitelistYamlPath;
    }

    public boolean getUseProxy() {

        boolean available = false;

        try {

            available = storage.getBoolean("useProxy");

        } catch (Exception e) {

            LOGGER.error("error parsing config for proxy setting: useProxy", e);

        }
        return available;
    }

    public boolean getProxyConfigured() {

        try {

            String proxyHost = getProxyHost();
            if (proxyHost == null || proxyHost.trim().equals("")) {
                return false;
            }
            return storage.getInt("proxyPort", -1) != -1;
        } catch (Exception e) {

            LOGGER.error("error parsing config for proxy settings", e);
            return false;
        }

    }

    public boolean getUseHttpForS3() {
        try {

            return storage.getBoolean("useHTTPForS3", false);

        } catch (Exception e) {

            LOGGER.error("error parsing config for S3 http(s) scheme settings", e);
            return false;
        }
    }

    public String getSmtpHostname() {
        return storage.getString("smtpHostname");
    }

    public int getSmtpPort() {
        return storage.getInt("smtpPort");
    }

    public String getSmtpUser() {
        return storage.getString("smtpUser");
    }

    public String getSmtpPassword() {
        return storage.getString("smtpPassword");
    }

    public String getSmtpAuth() {
        return storage.getString("smtpAuth");
    }

    public String getSmtpStartTlsEnable() {
        return storage.getString("smtpStartTlsEnable");
    }

    public String getSmtpSocketFactoryClass() {
        return storage.getString("smtpSocketFactoryClass");
    }

    public String getSmtpSocketFactoryFallback() {
        return storage.getString("smtpSocketFactoryFallback");
    }

    public String getTestTeamCoord() {
        return storage.getString("com.pkb.test.testTeamCoord", "");
    }

    public String getTestClinician() {
        return storage.getString("com.pkb.test.testClinician", "az.pro.1@pkbtest.com");
    }

    public String getTestPatient() {
        return storage.getString("com.pkb.test.testPatient", "az.patient.1@pkbtest.com");
    }

    public int getLetterInvitationTokenSize() {
        return storage.getInt("letter.invitation.token.size", LetterInvitationSettings.DEFAULT_TOKEN_SIZE);
    }

    public int getLetterInvitationAccessCodeSize() {
        return storage.getInt("letter.invitation.access.code.size", LetterInvitationSettings.DEFAULT_ACCESS_CODE_SIZE);
    }

    public int getLetterInvitationExpiry() {
        return storage.getInt("letter.invitation.token.expiry", LetterInvitationSettings.DEFAULT_EXPIRY_IN_SECONDS);
    }

    public long getUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxFileSize", 10000000000L);
    }

    public String getUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxFileText", "10");
    }

    public long getImageUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxImageFileSize", 1000000000L);
    }

    public String getEmisEsJobCron() {
        return storage.getString("emisEsJobCron");
    }

    public String getSciStoreNotificationCron() {
        return storage.getString("sciStoreNotificationCron");
    }

    public String getScriStorePatientSyncCron() {
        return storage.getString("sciStorePatientSyncCron");
    }

    public String getChildBirthNotificationCron() {
        return storage.getString("childBirthNotificationCron");
    }

    public String getExpiredConsentRemoverCron() {
        return storage.getString("expiredConsentRemoverCron");
    }

    public String getSymptomsNotificationCron() {
        return storage.getString("symptomsNotificationCron");
    }

    public String getUnreadNotificationCron() {
        return storage.getString("unreadNotificationCron");
    }

    public String getImageUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxImageFileText", "1 GB");
    }

    public long getGeneticsUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxGeneticsFileSize", 1000000000L);
    }

    public String getGeneticsUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxGeneticsFileTextGB", "1");
    }

    public long getPlanUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxPlanFileSize", 20000000L);
    }

    public String getPlanUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxPlanFileTextMB", "20");
    }

    public boolean isEmisEsEnabled() {
        return storage.getBoolean("feature.emis.es.enabled");
    }

    public String getAuthorizationEndpointAddress() {
        return storage.getString("authorization.endpoint.address", "http://localhost");
    }

    public String getRestApiClientId() {
        return storage.getString("com.pkb.api.util.DefaultInternalAccessTokenIssuer.clientId");
    }

    public String getRestApiClientIdForSPA() {
        return storage.getString("rest.api.client.id.singlePageApp", "pkb-spa-user");
    }

    public String getEMISSSOMonitoringProfEmail() {
        return storage.getString("emis.sso.monitoring.prof.email");
    }

    public boolean isTimelineEnabled() {
        return storage.getBoolean("feature.timeline.enabled", true);
    }

    public boolean isTimelineCalendarEnabled() {
        return storage.getBoolean("feature.timeline.calendar.enabled", false);
    }

    public boolean isTimelineTestsEnabled() {
        return storage.getBoolean("feature.timeline.tests.enabled", true);
    }

    public boolean isTimelineRadiologyEnabled() {
        return storage.getBoolean("feature.timeline.radiology.enabled", true);
    }

    public boolean isTimelineSymptomsEnabled() {
        return storage.getBoolean("feature.timeline.symptoms.enabled", false);
    }

    public boolean isTimelineMeasurementsEnabled() {
        return storage.getBoolean("feature.timeline.measurements.enabled", false);
    }

    public int getTimelineRequestTimeout() {
        return storage.getInt("feature.timeline.request.timeout", 10000);
    }

    public boolean isPatientBannerEnabled() {
        return storage.getBoolean("feature.patientbanner.enabled", true);
    }

    public String getTimelineFrontendFetchURL() {
        return storage.getString("timelineFrontendFetchURL");
    }

    public String getTimelineFrontendBrowserBaseURL() {
        return storage.getString("timelineFrontendBrowserBaseURL");
    }

    public int getTimelineHtmlExpiryInSeconds() {
        return storage.getInt("timelineHtmlExpiryInSeconds");
    }

    public String getValidicURL() {
        return storage.getString("validicUrl");
    }

    public int getValidicConnectionTimeoutMillis() {
        return storage.getInt("validicConnectionTimeoutMillis");
    }

    public int getValidicReadTimeoutMillis() {
        return storage.getInt("validicReadTimeoutMillis");
    }

    public long getComposeMessageMaxFileSize() {
        return storage.getLong("com.pkb.upload.ComposeMessage.maxFileSize");
    }

    public String getComposeMessageFileSizeText() {
        return storage.getString("com.pkb.upload.ComposeMessage.maxFileText");
    }

    public boolean isCSRFProtectionEnabled() {
        return storage.getBoolean("csrf.protection.enabled");
    }

    public boolean isTrackingEnabled() {
        return storage.getBoolean("trackingEnabled", false);
    }

    public boolean isScistoreEnabled() {
        return storage.getBoolean("feature.scistore.enabled", false);
    }

    @Override
    public int getMenudataQueryBatchSize() {
        return storage.getInt("menudataQueryBatchSize", 5000);
    }

    public int getDBQueryBatchSize() {
        return storage.getInt("dbQueryBatchSize", 10000);
    }

    public boolean isClientCachingOfStaticFilesEnabled() {
        return storage.getBoolean("clientCachingOfStaticFiles");
    }

    public boolean isDisplayOfSensitiveErrorInformationEnabled() {
        return storage.getBoolean("security.display.sensitive.error.info.enabled", false);
    }

    public boolean isSkipHL7IpWhitelistEnabled() {
        return storage.getBoolean("security.skip.hl7.whitelist.enabled", false);
    }

    public boolean isTestUsersEnabled() {
        return storage.getBoolean("security.test.users.enabled", false);
    }

    public boolean isHl7QryA19StaEnabled() {
        return storage.getBoolean("feature.hl7.a19.sta.enabled", false);
    }

    public int getHl7QryA19PageSize() {
        return storage.getInt("feature.hl7.a19.sta.pagesize", 10000);
    }

    public boolean isHL7MdmT11DocumentDeletionEnabled() {
        return storage.getBoolean("feature.hl7.document.deletion.enabled", false);
    }

    public int getNumberOfRecentLoginAttemptsAllowed() {
        return storage.getInt("numberOfRecentLoginAttemptsAllowed");
    }

    public int getDefinitionOfRecentLoginAttemptInSeconds() {
        return storage.getInt("definitionOfRecentLoginAttemptInSeconds");
    }

    @Override
    public boolean isFhirApiExperimental() {
        return storage.getBoolean("fhir.api.experimental", false);
    }

    public int getEmisEsProcessingBatchSize() {
        return storage.getInt("emisEsBatchSize");
    }

    public int getEmisEsCodingBatchSize() {
        return storage.getInt("emisEsCodingBatchSize", 4000);
    }

    public boolean isEmisEsStateResumeEnabled() {
        return storage.getBoolean("emisEsStateResumeEnabled", true);
    }

    public int getAutosaveTimeoutInMilliseconds() {
        return storage.getInt("ui.autosaveTimeoutInMilliseconds");
    }

    public int getAppointmentsCalendarMonths() {
        return storage.getInt("appointments.calendar.months", 6);
    }

    @Override
    public boolean isFhirAppointmentResourceEnabled() {
        return storage.getBoolean("fhir.api.Appointment.enabled", true);
    }

    @Override
    public boolean isFhirEncounterResourceEnabled() {
        return storage.getBoolean("fhir.api.Encounter.enabled", false);
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
    public boolean isFhirApiEnabled() {
        return storage.getBoolean("fhir.api.enabled", true);
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

    public Optional<String> clamAvHost() {
        Optional<String> maybeHostName = empty();

        if (storage.getBoolean("clamav.scanning.enabled", true)) {
            String host = storage.getString("clamav.host", "clamav");

            if (host.trim().isEmpty()) {
                throw new IllegalStateException("'clamav.host' is blank");
            }

            maybeHostName = Optional.of(host);
        }

        LOGGER.info("Virus Scanning is {}enabled{}",
                maybeHostName.map($ -> "").orElse("not "),
                maybeHostName.map(hostname -> format(", using hostname=[%s].", hostname)).orElse(""));

        return maybeHostName;
    }

    @Override
    public boolean isFakeDateTimeServiceEnabled() {
        return storage.getBoolean("fakedatetimeservice.enabled", false);
    }

    @Override
    public String getVcMaxWarningDate() {
        return storage.getString("vc_max_warning_date", "2018-07-26T00:00:00Z");
    }

    public boolean isFakeHelpPageBaseUrlProviderEnabled() {
        return storage.getBoolean("fakehelppagebaseurlprovider.enabled", false);

    }

    @Override
    public boolean isFhirDocumentReferenceResourceEnabled() {
        return storage.getBoolean("fhir.api.DocumentReference.enabled", true);
    }

    public int getEncounterTimelineRangeMonths() {
        return storage.getInt("encounterTimelineRangeMonths", 6);
    }

    public int getUserAgentAnalyzerCacheSize() {
        return storage.getInt("userAgentAnalyzerCacheSize", 1000);
    }

    public boolean enforceSecureCookies() {
        return storage.getBoolean("feature.enforce.secure.cookies", true);
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
    public int getMaxBulkInvitationOutcomeReport() {
        return storage.getInt("com.pkb.patient.invitation.maxBulkInvitationOutcomeReport", 300);
    }

    @Override
    public boolean isExceptionForNullUniqueIdEnabled() {
        return storage.getBoolean("consistency.check.data.point.unique.id.null.exception", true);
    }

    @Override
    public boolean isExceptionForMissingSourceEnabled() {
        return storage.getBoolean("consistency.check.data.point.source.null.exception", true);
    }


    @Override
    public boolean isExceptionForNoUniqueIdInMessageEnabled() {
        return storage.getBoolean("consistency.check.message.unique.id.null.exception", true);
    }

    public String getHospitalMapIframeSrc() {
        return storage.getString("webapp.hospitalMapIframeSrc", "https://viewer.blipstar.com/show?uid=1832224&search=geoip&gui=true&rc=&width=auto&tag=false");
    }

    @Override
    public String getSynertecApiClientId() {
        return storage.getString("synertecApiClientId", "synertec-user-test");
    }

    public int orgNetworkSyncTransactionTimeout() {
        return storage.getInt("orgnetwork.sync.transaction.timeout.seconds", (int) MINUTES.toSeconds(20));
    }
}

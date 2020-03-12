package com.pkb.common.config;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ImmutableConfig implements ConfigV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RawConfigStorage storage;

    public ImmutableConfig(RawConfigStorage storage) {
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


    @Override
    public String getFromEmailAddress() {
        return storage.getString("fromEmailAddress", "");
    }

    @Override
    public String getFromEmailName() {
        return storage.getString("fromEmailName", "");
    }

    @Override
    public int getInboxPageSize() {
        return storage.getInt("inboxPageSize");
    }

    @Override
    public int getPatientFilesPerPage() {
        return storage.getInt("patientFilesPerPage");
    }

    @Override
    public int getPatientDiaryEntriesPerPage() {
        return storage.getInt("patientDiaryEntriesPerPage");
    }

    @Override
    public String getPKBLogoURL() {
        return storage.getString("pkbLogoURL", "");
    }

    @Override
    public String getTestEmailId() {
        return storage.getString("testEmailId", "");
    }

    @Override
    public String getHelpPhone() {
        return storage.getString("helpPhoneNumber", "");
    }

    @Override
    public String getHelpEmailAddress() {
        return storage.getString("helpEmailAddress", "");
    }

    @Override
    public String getHelpWebSite() {
        return storage.getString("helpWebSite", "");
    }

    /** always ends with / if there's a value (enforced in this method) */
    @Override
    public String getInstituteLogoBaseDir() {
        String baseUrl = storage.getString("instituteLogoBaseDirectory", "");
        if (isNotBlank(baseUrl) && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }

    @Override
    public String getPHPlanTemplateImageBaseDir() {
        return storage.getString("pHPlanTemplateImageBaseDirectory", "");
    }

    @Override
    public Path getImageUploadLogoBaseDirectory() {
        return Paths.get(storage.getString("imageUploadLogoBaseDirectory", ""));
    }

    @Override
    public String getImageUploadPHPlanTemplateImageBaseDirectory() {
        return storage.getString("imageUploadPHPlanTemplateImageBaseDirectory", "");
    }

    @Override
    public String getHelpName() {
        return storage.getString("helpName", "");
    }

    @Override
    public int getRecipientColumnSize() {
        return storage.getInt("recipientColumnSize", 3);
    }

    @Override
    public int getThumbnailImageWidth() {
        return storage.getInt("thumbnailImageWidth", 400);
    }

    @Override
    public int getThumbnailImageHeight() {
        return storage.getInt("thumbnailImageHeight", 400);
    }

    @Override
    public int getRadiologyReportExtractLength() {
        return storage.getInt("radiologyReportExtractLength", 200);
    }

    @Override
    public String getThumbnailImageFormat() {
        return storage.getString("thumbnailImageFormat", "png");
    }

    @Override
    public boolean getSendReminderEmailsEnabled() {
        return storage.getBoolean("sendReminderEmailsEnabled", false);
    }

    @Override
    public boolean getUnreadMessageReminderEnabled() {
        return storage.getBoolean("sendUnreadMessageReminderEnabled", false);
    }

    @Override
    public boolean getUnreadDocumentReminderEnabled() {
        return storage.getBoolean("sendUnreadDocumentReminderEnabled", true);
    }

    @Override
    public String getWithingsOauthKey() {
        return storage.getString("withingsOauthKey", "");
    }

    @Override
    public String getWithingsOauthSecret() {
        return storage.getString("withingsOauthSecret", "");
    }

    @Override
    public String getVitaDockOauthKey() {
        return storage.getString("vitaDockOauthKey", "");
    }

    @Override
    public String getVitaDockOauthSecret() {
        return storage.getString("vitaDockOauthSecret", "");
    }

    @Override
    public String getSciStoreEndpoint() {
        return storage.getString("sciStoreEndpoint");
    }

    @Override
    public String getSciStoreUsername() {
        return storage.getString("sciStoreUsername");
    }

    @Override
    public String getSciStorePassword() {
        return storage.getString("sciStorePassword");
    }

    @Override
    public String getFakeS3Endpoint() {
        return storage.getString("fakeS3Endpoint");
    }

    @Override
    public boolean isDefinedFakeS3Endpoint() {
        return storage.containsKey("fakeS3Endpoint");
    }

    @Override
    public String getFakeS3EndpointPublishedURL() {
        return storage.getString("fakeS3EndpointPublishedURL");
    }

    @Override
    public int getOrgAdminDashboardPageSize() {
        return storage.getInt("orgAdminDashboardPageSize");
    }

    @Override
    public int getAccessLogPageSize() {
        return storage.getInt("accessLogPageSize");
    }

    @Override
    public String getCoreDevicesOrganizationId() {
        return storage.getString("coreDevicesOrganizationId");
    }

    @Override
    public String getCoreDevicesAccessToken() {
        return storage.getString("coreDevicesAccessToken");
    }

    @Override
    public int getSciStoreNewPatientLimit() {
        return storage.getInt("integration.scistore.newpatientlimit", 0);
    }

    @Override
    public int getLatestResultsInitialThreshold() {
        return storage.getInt("latestResultsInitialThreshold");
    }

    @Override
    public int getHighchartsThreshold() {
        return storage.getInt("highchartsThreshold");
    }

    @Override
    public boolean getUseUTCInHighcharts() {
        return storage.getBoolean("useUTCInHighcharts", false);
    }

    @Override
    public boolean getSortEmisCsvs() {
        return storage.getBoolean("integrations.emis.csv.sort", true);
    }

    @Override
    public String getLabtestsonlineSearchURL() {
        return storage.getString("labtestsonlineSearchURL");
    }

    @Override
    public String getLabtestsonlineRefURL() {
        return storage.getString("labtestsonlineRefURL");
    }

    @Override
    public String getHelpTextURL() {
        return storage.getString("helpTextURL");
    }

    @Override
    public int getFileChunkSizeInBytes() {
        return Integer.parseInt(storage.getString("fileChunkSizeInBytes"));
    }

    @Override
    public int getDocDeleteBatchSize() {
        return Integer.parseInt(storage.getString("docDeleteBatchSize", "20"));
    }

    @Override
    public long getPatientBannerErrorTimeoutMillis() {
        return storage.getLong("feature.patientbanner.errorTimeoutMillis", 21000L);
    }

    @Override
    public int getDocDeleteIntervalInHours() {
        return Integer.parseInt(storage.getString("docDeleteIntervalInHours", "8"));
    }

    @Override
    public Boolean getPrometheusReportingEnabled() {
        return storage.getBoolean("prometheusReportingEnabled", Boolean.TRUE);
    }

    @Override
    public String getLibraryUploadDirectory() {
        return storage.getString("libraryUploadBaseDirectory");
    }

    @Override
    public String getNonCryptoAccessKey() {
        return storage.getString("nonCryptoAccessKey");
    }

    @Override
    public String getNonCryptoSecretKey() {
        return storage.getString("nonCryptoSecretKey");
    }

    @Override
    public String getNonCryptoBucket() {
        return storage.getString("nonCryptoBucket");
    }

    @Override
    public String getProxyHost() {
        return storage.getString("proxyHost");
    }

    @Override
    public int getProxyPort() {
        return storage.getInt("proxyPort");
    }

    @Override
    public String getEmisEsDownloadBaseDir() {
        return storage.getString("emisEsDownloadBaseDir");
    }

    @Override
    public String getEmisEsPgpPrivateKeyFile() {
        return storage.getString("emisEsPgpPrivateKeyFile");
    }

    @Override
    public String getEmisEsPgpPrivateKeyFilePassphrase() {
        return storage.getString("emisEsPgpPrivateKeyFilePassphrase", "");
    }

    @Override
    public String getEmisEsCsvEncoding() {
        return storage.getString("emisEsCsvEncoding", "UTF-8");
    }

    @Override
    public Boolean isEmisEsWhiteListEnabled() {
        return storage.getBoolean("emisEsWhiteListEnabled", true);
    }

    @Override
    public int getEmisFailureGracePeriodHours() {
        return storage.getInt("emisEsFailureGracePeriodHours");
    }

    @Override
    public Path getEmisEsSftpConfigYamlPath() {
        return Paths.get(storage.getString("emisEsSftpConfigYaml"));
    }

    @Override
    public Optional<Path> getEmisEsWhitelistYamlPath() {

        Optional<Path> whitelistYamlPath = empty();

        if (storage.getString("emisEsWhitelistYaml") != null) {
            whitelistYamlPath = Optional.of(Paths.get(storage.getString("emisEsWhitelistYaml")));

        }
        return whitelistYamlPath;
    }

    @Override
    public boolean getUseProxy() {

        boolean available = false;

        try {

            available = storage.getBoolean("useProxy");

        } catch (Exception e) {

            LOGGER.error("error parsing config for proxy setting: useProxy", e);

        }
        return available;
    }

    @Override
    public boolean isProxyConfigured() {

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

    @Override
    public boolean getUseHttpForS3() {
        try {

            return storage.getBoolean("useHTTPForS3", false);

        } catch (Exception e) {

            LOGGER.error("error parsing config for S3 http(s) scheme settings", e);
            return false;
        }
    }

    @Override
    public String getSmtpHostname() {
        return storage.getString("smtpHostname");
    }

    @Override
    public int getSmtpPort() {
        return storage.getInt("smtpPort");
    }

    @Override
    public String getSmtpUser() {
        return storage.getString("smtpUser");
    }

    @Override
    public String getSmtpPassword() {
        return storage.getString("smtpPassword");
    }

    @Override
    public String getSmtpAuth() {
        return storage.getString("smtpAuth");
    }

    @Override
    public String getSmtpStartTlsEnable() {
        return storage.getString("smtpStartTlsEnable");
    }

    @Override
    public String getSmtpSocketFactoryClass() {
        return storage.getString("smtpSocketFactoryClass");
    }

    @Override
    public String getSmtpSocketFactoryFallback() {
        return storage.getString("smtpSocketFactoryFallback");
    }

    @Override
    public String getTestTeamCoord() {
        return storage.getString("com.pkb.test.testTeamCoord", "");
    }

    @Override
    public String getTestClinician() {
        return storage.getString("com.pkb.test.testClinician", "az.pro.1@pkbtest.com");
    }

    @Override
    public String getTestPatient() {
        return storage.getString("com.pkb.test.testPatient", "az.patient.1@pkbtest.com");
    }

    @Override
    public int getLetterInvitationTokenSize() {
        return storage.getInt("letter.invitation.token.size", LetterInvitationSettings.DEFAULT_TOKEN_SIZE);
    }

    @Override
    public int getLetterInvitationAccessCodeSize() {
        return storage.getInt("letter.invitation.access.code.size", LetterInvitationSettings.DEFAULT_ACCESS_CODE_SIZE);
    }

    @Override
    public int getLetterInvitationExpiry() {
        return storage.getInt("letter.invitation.token.expiry", LetterInvitationSettings.DEFAULT_EXPIRY_IN_SECONDS);
    }

    @Override
    public long getUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxFileSize", 10000000000L);
    }

    @Override
    public String getUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxFileText", "10");
    }

    @Override
    public long getImageUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxImageFileSize", 1000000000L);
    }

    @Override
    public String getEmisEsJobCron() {
        return storage.getString("emisEsJobCron");
    }

    @Override
    public String getSciStoreNotificationCron() {
        return storage.getString("sciStoreNotificationCron");
    }

    @Override
    public String getScriStorePatientSyncCron() {
        return storage.getString("sciStorePatientSyncCron");
    }

    @Override
    public String getChildBirthNotificationCron() {
        return storage.getString("childBirthNotificationCron");
    }

    @Override
    public String getExpiredConsentRemoverCron() {
        return storage.getString("expiredConsentRemoverCron");
    }

    @Override
    public String getSymptomsNotificationCron() {
        return storage.getString("symptomsNotificationCron");
    }

    @Override
    public String getUnreadNotificationCron() {
        return storage.getString("unreadNotificationCron");
    }

    @Override
    public String getImageUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxImageFileText", "1 GB");
    }

    @Override
    public long getGeneticsUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxGeneticsFileSize", 1000000000L);
    }

    @Override
    public String getGeneticsUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxGeneticsFileTextGB", "1");
    }

    @Override
    public long getPlanUploadMaxFileSize() {
        return storage.getLong("com.pkb.upload.maxPlanFileSize", 20000000L);
    }

    @Override
    public String getPlanUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxPlanFileTextMB", "20");
    }

    @Override
    public boolean isEmisEsEnabled() {
        return storage.getBoolean("feature.emis.es.enabled");
    }

    @Override
    public boolean getBulkDisableSharingEnabled() {
        return storage.getBoolean("feature.bulkdisablesharing.enabled", false);
    }

    @Override
    public String getAuthorizationEndpointAddress() {
        return storage.getString("authorization.endpoint.address", "http://localhost");
    }

    @Override
    public String getRestApiClientId() {
        return storage.getString("com.pkb.api.util.DefaultInternalAccessTokenIssuer.clientId", "pkb-spa-user");
    }

    @Override
    public String getEMISSSOMonitoringProfEmail() {
        return storage.getString("emis.sso.monitoring.prof.email");
    }

    @Override
    public boolean isRollBarEnabled() {
        return storage.getBoolean("feature.rollbar.enabled", false);
    }

    @Override
    public @Nullable String getRollBarEnvironment() {
        // There is no sensible default for production as we have multiple deployments.
        return getConditionalRollbarProperty("feature.rollbar.environment", "environment");
    }

    @Override
    public @Nullable String getRollbarEndpoint() {
        return getConditionalRollbarProperty("feature.rollbar.endpoint", "endpoint");
    }

    @Override
    public boolean isTimelineEnabled() {
        return storage.getBoolean("feature.timeline.enabled", true);
    }

    @Override
    public boolean isTimelineCalendarEnabled() {
        return storage.getBoolean("feature.timeline.calendar.enabled", true);
    }

    @Override
    public boolean isTimelineTestsEnabled() {
        return storage.getBoolean("feature.timeline.tests.enabled", true);
    }

    @Override
    public boolean isTimelineRadiologyEnabled() {
        return storage.getBoolean("feature.timeline.radiology.enabled", true);
    }

    @Override
    public boolean isTimelineSymptomsEnabled() {
        return storage.getBoolean("feature.timeline.symptoms.enabled", false);
    }

    @Override
    public boolean isTimelineMeasurementsEnabled() {
        return storage.getBoolean("feature.timeline.measurements.enabled", false);
    }

    @Override
    public boolean isTimelinePreloadingEnabled() {
        return storage.getBoolean("feature.timeline.preloading.enabled", false);
    }

    @Override
    public int getTimelineRequestTimeout() {
        return storage.getInt("feature.timeline.request.timeout", 10000);
    }

    @Override
    public boolean isPatientBannerEnabled() {
        return storage.getBoolean("feature.patientbanner.enabled", true);
    }

    @Override
    public boolean isRejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3() {
        return storage.getBoolean("feature.reject.nontext.hl7.oru.r01.with.one.obr.having.multiple.obx3", false);
    }

    @Override
    public String getTimelineFrontendFetchURL() {
        return storage.getString("timelineFrontendFetchURL");
    }

    @Override
    public String getTimelineFrontendBrowserBaseURL() {
        return storage.getString("timelineFrontendBrowserBaseURL");
    }

    @Override
    public int getTimelineHtmlExpiryInSeconds() {
        return storage.getInt("timelineHtmlExpiryInSeconds");
    }

    @Override
    public String getValidicURL() {
        return storage.getString("validicUrl");
    }

    @Override
    public int getValidicConnectionTimeoutMillis() {
        return storage.getInt("validicConnectionTimeoutMillis");
    }

    @Override
    public int getValidicReadTimeoutMillis() {
        return storage.getInt("validicReadTimeoutMillis");
    }

    @Override
    public long getComposeMessageMaxFileSize() {
        return storage.getLong("com.pkb.upload.ComposeMessage.maxFileSize");
    }

    @Override
    public String getComposeMessageFileSizeText() {
        return storage.getString("com.pkb.upload.ComposeMessage.maxFileText");
    }

    @Override
    public boolean isCSRFProtectionEnabled() {
        return storage.getBoolean("csrf.protection.enabled");
    }

    @Override
    public boolean isTrackingEnabled() {
        return storage.getBoolean("trackingEnabled", false);
    }

    @Override
    public int getMatomoSiteId() {
        return storage.getInt("matomo.siteId");
    }

    @Override
    public boolean isScistoreEnabled() {
        return storage.getBoolean("feature.scistore.enabled", false);
    }

    @Override
    public int getMenudataQueryBatchSize() {
        return storage.getInt("menudataQueryBatchSize", 5000);
    }

    @Override
    public int getDBQueryBatchSize() {
        return storage.getInt("dbQueryBatchSize", 10000);
    }

    @Override
    public boolean isClientCachingOfStaticFilesEnabled() {
        return storage.getBoolean("clientCachingOfStaticFiles");
    }

    @Override
    public boolean isDisplayOfSensitiveErrorInformationEnabled() {
        return storage.getBoolean("security.display.sensitive.error.info.enabled", false);
    }

    @Override
    public boolean isHL7IpWhitelistEnabled() {
        return storage.getBoolean("security.hl7.whitelist.enabled", true);
    }

    @Override
    public boolean isTestUsersEnabled() {
        return storage.getBoolean("security.test.users.enabled", false);
    }

    @Override
    public int getHl7QryA19PageSize() {
        return storage.getInt("feature.hl7.a19.sta.pagesize", 10000);
    }

    @Override
    public boolean isHL7MdmT11DocumentDeletionEnabled() {
        return storage.getBoolean("feature.hl7.document.deletion.enabled", true);
    }

    @Override
    public boolean isHL7MdmT01DocumentUpdateEnabled() {
        return storage.getBoolean("feature.hl7.document.update.enabled", true);
    }

    @Override
    public int getNumberOfRecentLoginAttemptsAllowed() {
        return storage.getInt("numberOfRecentLoginAttemptsAllowed");
    }

    @Override
    public int getDefinitionOfRecentLoginAttemptInSeconds() {
        return storage.getInt("definitionOfRecentLoginAttemptInSeconds");
    }

    @Override
    public boolean isFhirApiExperimental() {
        return storage.getBoolean("fhir.api.experimental", false);
    }

    @Override
    public int getEmisEsProcessingBatchSize() {
        return storage.getInt("emisEsBatchSize");
    }

    @Override
    public int getEmisEsCodingBatchSize() {
        return storage.getInt("emisEsCodingBatchSize", 4000);
    }

    @Override
    public boolean isEmisEsStateResumeEnabled() {
        return storage.getBoolean("emisEsStateResumeEnabled", true);
    }

    @Override
    public int getAutosaveTimeoutInMilliseconds() {
        return storage.getInt("ui.autosaveTimeoutInMilliseconds");
    }

    @Override
    public int getAppointmentsCalendarMonths() {
        return storage.getInt("appointments.calendar.months", 6);
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

    @Override
    public boolean isFakeHelpPageBaseUrlProviderEnabled() {
        return storage.getBoolean("fakehelppagebaseurlprovider.enabled", false);

    }

    @Override
    public boolean isFhirDocumentReferenceResourceEnabled() {
        return storage.getBoolean("fhir.api.DocumentReference.enabled", true);
    }

    @Override
    public int getEncounterTimelineRangeMonths() {
        return storage.getInt("encounterTimelineRangeMonths", 6);
    }

    @Override
    public int getUserAgentAnalyzerCacheSize() {
        return storage.getInt("userAgentAnalyzerCacheSize", 1000);
    }

    @Override
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
    public int getMaxBulkInvitationOutcomeReport() {
        return storage.getInt("com.pkb.patient.invitation.maxBulkInvitationOutcomeReport", 300);
    }

    @Override
    public boolean isExceptionForNullUniqueIdEnabled() {
        return storage.getBoolean("consistency.check.data.point.unique.id.null.exception", true);
    }

    @Override
    public boolean isExceptionForNullRouteEnabled() {
        return storage.getBoolean("consistency.check.data.point.route.null.exception", true);
    }

    @Override
    public boolean isExceptionForNotCapturedRouteEnabled() {
        return storage.getBoolean("consistency.check.data.point.route.notcaptured.exception", true);
    }

    @Override
    public boolean isExceptionForMissingSourceEnabled() {
        return storage.getBoolean("consistency.check.data.point.source.null.exception", true);
    }

    @Override
    public boolean isExceptionForNoUniqueIdInMessageEnabled() {
        return storage.getBoolean("consistency.check.message.unique.id.null.exception", true);
    }

    @Override
    public boolean isExceptionForMissingPatientParticipantEnabled() {
        return storage.getBoolean("consistency.check.message.missing.patient.participant.exception", true);
    }

    @Override
    public boolean isExceptionForNullRequestContextEnabled() {
        return storage.getBoolean("consistency.check.requestcontext.null.exception", true);
    }

    @Override
    public boolean isExceptionForNullDefaultAccountIdEnabled() {
        return storage.getBoolean("consistency.check.pkbperson.defaultaccountid.null.exception", false);
    }

    @Override
    public String getHospitalMapIframeSrc() {
        return storage.getString("webapp.hospitalMapIframeSrc", "https://viewer.blipstar.com/show?uid=1832224&search=geoip&gui=true&rc=&width=auto&tag=false");
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
    public int orgNetworkSyncTransactionTimeout() {
        return storage.getInt("orgnetwork.sync.transaction.timeout.seconds", (int) MINUTES.toSeconds(20));
    }

    @Override
    public String getEmisRestUrl() {
        return storage.getString("emis.rest.url", "http://emis-mock-api:8080/PFS");
    }

    @Override
    public String getEmisRestXapiApplicationId() {
        return storage.getString("emis.rest.xapi.application.id", "TESTAPPID");
    }

    @Override
    public String getRestEmisXapiVersion() {
        return storage.getString("emis.rest.xapi.version", "2.1.0.0");
    }

    @Override
    public int getEmisRestConnectTimeout() {
        return storage.getInt("emis.rest.connection.timeout", (int) SECONDS.toMillis(30));
    }

    @Override
    public String getEmisRestKeystore() {
        return storage.getString("emis.rest.keystore");
    }

    @Override
    public boolean isGpAppointmentBookingEnabled() {
        return storage.getBoolean("feature.gpAppointmentBooking.enabled", false);
    }

    @Override
    public boolean isGpPrescriptionOrderingEnabled() {
        return storage.getBoolean("feature.gpPrescriptionOrdering.enabled", false);
    }

    @Override
    public String uploadedDataSyncUrl() {
        return storage.getString("fhir.api.uploadeddata.sync.url", "http://phr-wf:80/synchUploadedData.action");
    }

    @Override
    public long uploadedDataSyncConnectionTimeoutMilliseconds() {
        return storage.getLong("fhir.api.uploadeddata.sync.connectiontimeout.milliseconds", SECONDS.toMillis(10));
    }

    @Override
    public long uploadedDataSyncWriteTimeoutMilliseconds() {
        return storage.getLong("fhir.api.uploadeddata.sync.write.milliseconds", SECONDS.toMillis(10));
    }

    @Override
    public long uploadedDataSyncReadTimeoutMilliseconds() {
        return storage.getLong("fhir.api.uploadeddata.sync.read.milliseconds", MINUTES.toMillis(2));
    }

    @Override
    public String getOdsRestUrl() {
        return storage.getString("ods.rest.url", "https://directory.spineservices.nhs.uk/STU3");
    }

    @Override
    public String getKMSBaseURL() {
        return storage.getString("kms.baseurl", "http://kms:8080");
    }

    @Override
    public boolean isExceptionForKmsFailureEnabled() {
        return storage.getBoolean("kms.exception.on.failure", true);
    }

    @Override
    public boolean isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled() {
        return storage.getBoolean("consistency.check.multiple.account.for.single.national.id.exception", false);
    }

    //The default value here is set to be a decent margin above the current number
    //of GP surgeries in the UK (~7k @ 2019-07)

    @Override
    public int getOdsCacheSize() {
        return storage.getInt("ods.cache.size", 10000);
    }

    @Override
    public int getOdsCacheExpiryHours() {
        return storage.getInt("ods.cache.expiry.hours", 24);
    }

    @Override
    public String getMatomoBaseUrl() {
        return storage.getString("matomo.base.url", "/_stats");
    }

    @Override
    public boolean isKmsKeyLoadingEnabled() {
        return storage.getBoolean("kms.key.loading.enabled", false);
    }

    private @Nullable String getConditionalRollbarProperty(String key, String name) {
        String property = null;

        if (isRollBarEnabled()) {
            property = storage.getString(key);
            if (isBlank(property)) {
                throw new IllegalStateException(format("Rollbar is enabled, but %s (defined with key=[%s]) is not defined or it is blank", name, key));
            }
        }

        return property;
    }

    @Override
    public int getUnregisteredPatientsPageSize() {
        return storage.getInt("unregisteredPatientsPageSize", 50);
    }

    @Override
    public int getInstituteUserFetchSize() {
        return storage.getInt("instituteUserFetchSize", 10000);
    }

    @Override
    public int getAccountUserFetchSize() {
        return storage.getInt("accountUserFetchSize", 10000);
    }

    @Override
    public int getUploadedDataFetchSize() {
        return storage.getInt("uploadedDataFetchSize", 200);
    }

    @Override
    public int getUploadedDataBatchSyncSize() {
        return storage.getInt("uploadedDataBatchSyncSize", 200);
    }

    @Override
    public boolean isNhsLoginEnabled() {
        return storage.getBoolean("feature.nhslogin.enabled", false);
    }

    @Override
    public boolean isNhsLoginWebUiButtonEnabled() {
        boolean nhsLoginWebUiButtonEnabled = storage.getBoolean("nhslogin.webui.button.enabled", false);

        if (nhsLoginWebUiButtonEnabled) {
            String key = "nhslogin.integration.path";
            checkState(isNotBlank(storage.getString(key)), "NHS Login button on WEB UI is enabled, but: 'NHS Login integration path' referred with key=[{}] is not defined or is blank", key);
        }

        return nhsLoginWebUiButtonEnabled;
    }

    @Override
    public String getNhsIntegrationUrl() {
        return storage.getString("nhslogin.integration.path");
    }

    @Override
    public int getSlowDocRefQueryAlertThresholdSeconds() {
        return storage.getInt("slowDocRefQueryAlertThresholdSeconds", 30);
    }

    @Override
    public boolean isBookAppointmentWarningVisible() {
        return storage.getBoolean("feature.bookAppointmentWarning.visible", false);
    }

    @Override
    public boolean isMutableConfigEnabled() {
        return storage.getBoolean("mutableConfig.enabled", false);
    }

    @Override
    public void setValue(String key, String value) {  /* no-op */  }

    @Override
    public void reset() { /* no-op*/ }
}

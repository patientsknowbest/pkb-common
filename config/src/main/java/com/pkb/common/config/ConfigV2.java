package com.pkb.common.config;

import static java.lang.String.format;
import static java.util.Optional.empty;

import java.net.URL;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private final RawConfigStorage storage;

    private ConfigV2() {
        this(RawConfigStorage.createDefault());
    }

    public ConfigV2(RawConfigStorage storage) {
        this.storage = storage;
    }

    /**
     * @param protocol
     *         defaults to http
     * @param host
     *         valid value required
     * @param port
     *         defaults to none specified (defaults ports 80/443 if specified will be removed)
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

    public String getRecaptchaPrivateKey() {
        return storage.getString("recaptchaPrivateKey", "");
    }

    public String getRecaptchaPublicKey() {
        return storage.getString("recaptchaPublicKey", "");
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

    public String getImageUploadLogoBaseDirectory() {
        return storage.getString("imageUploadLogoBaseDirectory", "");
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

    public String getPacrEndpoint() {
        return storage.getString("pacrEndpoint");
    }

    public String getPacrClientId() {
        return storage.getString("pacrClientId");
    }

    public String getPacrClientUsername() {
        return storage.getString("pacrClientUsername");
    }

    public String getPacrClientPassword() {
        return storage.getString("pacrClientPassword");
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

    public String getEmisEsSftpHost() {
        return storage.getString("emisEsSftpHost");
    }

    public int getEmisEsSftpPort() {
        return storage.getInt("emisEsSftpPort");
    }

    public String getEmisEsSftpUser() {
        return storage.getString("emisEsSftpUser");
    }

    public String getEmisEsSftpPrivateKeyFile() {
        return storage.getString("emisEsSftpPrivateKeyFile");
    }

    public String getEmisEsSftpPrivateKeyFilePassphrase() {
        return storage.getString("emisEsSftpPrivateKeyFilePassphrase", "");
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

    public String getImageUploadMaxFileText() {
        return storage.getString("com.pkb.upload.maxImageFileText", "1");
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
        return storage.getBoolean("timelineEnabled", false);
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

    public int getNumberOfRecentLoginAttemptsAllowed() {
        return storage.getInt("numberOfRecentLoginAttemptsAllowed");
    }

    public int getDefinitionOfRecentLoginAttemptInSeconds() {
        return storage.getInt("definitionOfRecentLoginAttemptInSeconds");
    }

    @Override
    public boolean isFhirApiExperimental() {
        return storage.getBoolean("fhir.api.experimental");
    }

    public int getEmisEsProcessingBatchSize() {
        return storage.getInt("emisEsBatchSize");
    }

    public int getAutosaveTimeoutInMilliseconds() {
        return storage.getInt("ui.autosaveTimeoutInMilliseconds");
    }

    public int getAppointmentsCalendarMonths() {
        return storage.getInt("appointments.calendar.months", 6);
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

    public boolean isFakeDateTimeServiceEnabled() {
        return storage.getBoolean("fakedatetimeservice.enabled", false);
    }

    public boolean isFakeHelpPageBaseUrlProviderEnabled() {
        return storage.getBoolean("fakehelppagebaseurlprovider.enabled", false);
    }

    @Override
    public boolean isFhirDocumentReferenceResourceEnabled() {
        return storage.getBoolean("fhir.api.DocumentReference.enabled", false);
    }

    public int getEncounterTimelineRangeMonths() {
        return storage.getInt("encounterTimelineRangeMonths", 6);
    }
}

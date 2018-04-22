//------------------------------------------------------------------------------
//
// Copyright (c) 2009-2013 PatientsKnowBest, Inc. All Rights Reserved.
//
// $Id:  Config.java May 25, 2009 12:39:17 PM mahendera$
//
//------------------------------------------------------------------------------

package com.pkb.common.config;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines API to get application properties
 *
 * Use {@link ConfigV2}, makes code easier to test.
 */
@Deprecated
final class ConfigV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    static {
        appConfigInstance = RawConfigStorage.createDefault();
    }

    /**
     * Looks like a singleton, but it isn't. Use the default constructor instead.
     *
     * @return A Config object based on the default AppConfig ;(
     */
    @Deprecated
    public static ConfigV1 getConfig() {
            return new ConfigV1(appConfigInstance);
    }

    private static final RawConfigStorage appConfigInstance;

    private final RawConfigStorage appConfig;

    private ConfigV1() {
        appConfig = appConfigInstance;
    }

    private ConfigV1(RawConfigStorage config) {
        appConfig = config;
    }

    /**
     * @param protocol
     *            defaults to http
     * @param host
     *            valid value required
     * @param port
     *            defaults to none specified (defaults ports 80/443 if specified will be removed)
     * @return example: protocol://host:port/application, or protocol://host
     *         no trailing slash unless included in application parameter
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
            baseURL = new URL(appConfig.getString("baseURL"));
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid URL value for baseURL: " + appConfig.getString("baseURL"), e);
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

    public String getExchangeHostName() {
        return appConfig.getString("echangeHostName", "");
    }

    public String getIMAPUserId() {
        return appConfig.getString("imapUserId", "");
    }

    public String getIMAPPassword() {
        return appConfig.getString("imapPassword", "");
    }

    public boolean isIMAPSSLEnabled() {
        return appConfig.getBoolean("sslEnabled", true);
    }

    public String getIncomingMailProtocol() {
        return appConfig.getString("incomingmailProtocol", "imaps");
    }

    public int getIncomingMailPort() {
        return appConfig.getInt("incomingmailPort", 0);
    }

    public String getFromEmailAddress() {
        return appConfig.getString("fromEmailAddress", "");
    }

    public String getFromEmailName() {
        return appConfig.getString("fromEmailName", "");
    }

    public String getRecaptchaPrivateKey() {
        return appConfig.getString("recaptchaPrivateKey", "");
    }

    public String getRecaptchaPublicKey() {
        return appConfig.getString("recaptchaPublicKey", "");
    }

    public int getInboxPageSize() {
        return appConfig.getInt("inboxPageSize");
    }

    public int getPatientFilesPerPage() {
        return appConfig.getInt("patientFilesPerPage");
    }

    public int getPatientDiaryEntriesPerPage() {
        return appConfig.getInt("patientDiaryEntriesPerPage");
    }

    public String getPKBLogoURL() {
        return appConfig.getString("pkbLogoURL", "");
    }

    public String getTestEmailId() {
        return appConfig.getString("testEmailId", "");
    }

    public String getHelpPhone() {
        return appConfig.getString("helpPhoneNumber", "");
    }

    public String getHelpEmailAddress() {
        return appConfig.getString("helpEmailAddress", "");
    }

    public String getHelpWebSite() {
        return appConfig.getString("helpWebSite", "");
    }

    /** always ends with / if there's a value (enforced in this method) */
    public String getInstituteLogoBaseDir() {
        String baseUrl = appConfig.getString("instituteLogoBaseDirectory", "");
        if (isNotBlank(baseUrl) && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }

    private static boolean isNotBlank(String str) {
        return str != null && !str.trim().equals("");
    }

    public String getPHPlanTemplateImageBaseDir() {
        return appConfig.getString("pHPlanTemplateImageBaseDirectory", "");
    }

    public String getImageUploadLogoBaseDirectory() {
        return appConfig.getString("imageUploadLogoBaseDirectory", "");
    }

    public String getImageUploadPHPlanTemplateImageBaseDirectory() {
        return appConfig.getString("imageUploadPHPlanTemplateImageBaseDirectory", "");
    }

    public String getSftpBaseDir() {
        return appConfig.getString("sftpBaseDir", "");
    }

    public String getHelpName() {
        return appConfig.getString("helpName", "");
    }

    public int getRecipientColumnSize() {
        return appConfig.getInt("recipientColumnSize", 3);
    }

    public int getThumbnailImageWidth() {
        return appConfig.getInt("thumbnailImageWidth", 400);
    }

    public int getThumbnailImageHeight() {
        return appConfig.getInt("thumbnailImageHeight", 400);
    }

    public int getRadiologyReportExtractLength() {
        return appConfig.getInt("radiologyReportExtractLength", 200);
    }

    public String getThumbnailImageFormat() {
        return appConfig.getString("thumbnailImageFormat", "png");
    }

    public boolean getSendReminderEmailsEnabled() {
        return appConfig.getBoolean("sendReminderEmailsEnabled", false);
    }

    public String getWithingsOauthKey() {
        return appConfig.getString("withingsOauthKey", "");
    }

    public String getWithingsOauthSecret() {
        return appConfig.getString("withingsOauthSecret", "");
    }

    public String getVitaDockOauthKey() {
        return appConfig.getString("vitaDockOauthKey", "");
    }

    public String getVitaDockOauthSecret() {
        return appConfig.getString("vitaDockOauthSecret", "");
    }

    public String getSciStoreEndpoint() {
        return appConfig.getString("sciStoreEndpoint");
    }

    public String getSciStoreUsername() {
        return appConfig.getString("sciStoreUsername");
    }

    public String getSciStorePassword() {
        return appConfig.getString("sciStorePassword");
    }

    public String getPacrEndpoint() {
        return appConfig.getString("pacrEndpoint");
    }

    public String getPacrClientId() {
        return appConfig.getString("pacrClientId");
    }

    public String getPacrClientUsername() {
        return appConfig.getString("pacrClientUsername");
    }

    public String getPacrClientPassword() {
        return appConfig.getString("pacrClientPassword");
    }

    public String getFakeS3Endpoint() {
        return appConfig.getString("fakeS3Endpoint");
    }

    public boolean isDefinedFakeS3Endpoint() {
        return appConfig.containsKey("fakeS3Endpoint");
    }

    public String getFakeS3EndpointPublishedURL() {
        return appConfig.getString("fakeS3EndpointPublishedURL");
    }

    public int getOrgAdminDashboardPageSize() {
        return appConfig.getInt("orgAdminDashboardPageSize");
    }

    public String getCoreDevicesOrganizationId() {
        return appConfig.getString("coreDevicesOrganizationId");
    }

    public String getCoreDevicesAccessToken() {
        return appConfig.getString("coreDevicesAccessToken");
    }

    public int getSciStoreNewPatientLimit() {
        return appConfig.getInt("integration.scistore.newpatientlimit", 0);
    }

    public int getLatestResultsInitialThreshold() {
        return appConfig.getInt("latestResultsInitialThreshold");
    }

    public int getHighchartsThreshold() {
        return appConfig.getInt("highchartsThreshold");
    }

    public boolean getSortEmisCsvs() {
        return appConfig.getBoolean("integrations.emis.csv.sort", true);
    }

    public String getLabtestsonlineSearchURL() {
        return appConfig.getString("labtestsonlineSearchURL");
    }

    public String getLabtestsonlineRefURL() {
        return appConfig.getString("labtestsonlineRefURL");
    }

    public String getHelpTextURL() {
        return appConfig.getString("helpTextURL");
    }

    public int getFileChunkSizeInBytes() {
        return Integer.parseInt(appConfig.getString("fileChunkSizeInBytes"));
    }

    public int getDocDeleteBatchSize() {
        return Integer.parseInt(appConfig.getString("docDeleteBatchSize", "20"));
    }

    public int getDocDeleteIntervalInHours() {
        return Integer.parseInt(appConfig.getString("docDeleteIntervalInHours", "8"));
    }

    public Boolean getPrometheusReportingEnabled() {
        return appConfig.getBoolean("prometheusReportingEnabled", Boolean.TRUE);
    }

    public String getLibraryUploadDirectory() {
        return appConfig.getString("libraryUploadBaseDirectory");
    }

    public String getNonCryptoAccessKey() {
        return appConfig.getString("nonCryptoAccessKey");
    }

    public String getNonCryptoSecretKey() {
        return appConfig.getString("nonCryptoSecretKey");
    }

    public String getNonCryptoBucket() {
        return appConfig.getString("nonCryptoBucket");
    }

    public String getProxyHost() {
        return appConfig.getString("proxyHost");
    }

    public int getProxyPort() {
        return appConfig.getInt("proxyPort");
    }

    public String getEmisEsDownloadBaseDir() {
        return appConfig.getString("emisEsDownloadBaseDir");
    }

    public String getEmisEsSftpHost() {
        return appConfig.getString("emisEsSftpHost");
    }

    public int getEmisEsSftpPort() {
        return appConfig.getInt("emisEsSftpPort");
    }

    public String getEmisEsSftpUser() {
        return appConfig.getString("emisEsSftpUser");
    }

    public String getEmisEsSftpPrivateKeyFile() {
        return appConfig.getString("emisEsSftpPrivateKeyFile");
    }

    public String getEmisEsSftpPrivateKeyFilePassphrase() {
        return appConfig.getString("emisEsSftpPrivateKeyFilePassphrase", "");
    }

    public String getEmisEsSftpRemoteDirectory() {
        return appConfig.getString("emisEsSftpRemoteDirectory");
    }

    public String getEmisEsPgpPrivateKeyFile() {
        return appConfig.getString("emisEsPgpPrivateKeyFile");
    }

    public String getEmisEsPgpPrivateKeyFilePassphrase() {
        return appConfig.getString("emisEsPgpPrivateKeyFilePassphrase", "");
    }

    public Boolean isEmisEsNotificationEnabled() {
        return appConfig.getBoolean("emisEsNotificationEnabled", false);
    }

    public Set<String> getEmisEsPatientIdentifierFilterSet() {
        String[] list = appConfig.getString("emisEsPatientIdentifierFilterList", "").split(",");
        return new HashSet<>(Arrays.asList(list));
    }

    public String getEmisEsCsvEncoding() {
        return appConfig.getString("emisEsCsvEncoding", "UTF-8");
    }

    public String getEmisEsConfigBaseDir() {
        return appConfig.getString("emisEsConfigBaseDir", "/pkb/emis");
    }

    public Boolean isEmisEsWhiteListEnabled() {
        return appConfig.getBoolean("emisEsWhiteListEnabled", true);
    }

    public int getEmisFailureGracePeriodHours() {
        return appConfig.getInt("emisEsFailureGracePeriodHours");
    }

    public boolean getUseProxy() {

        boolean available = false;

        try {

            available = appConfig.getBoolean("useProxy");

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
            return appConfig.getInt("proxyPort", -1) != -1;
        } catch (Exception e) {

            LOGGER.error("error parsing config for proxy settings", e);
            return false;
        }

    }

    public boolean getUseHttpForS3() {
        try {

            return appConfig.getBoolean("useHTTPForS3", false);

        } catch (Exception e) {

            LOGGER.error("error parsing config for S3 http(s) scheme settings", e);
            return false;
        }
    }

    public String getSmtpHostname() {
        return appConfig.getString("smtpHostname");
    }

    public int getSmtpPort() {
        return appConfig.getInt("smtpPort");
    }

    public String getSmtpUser() {
        return appConfig.getString("smtpUser");
    }

    public String getSmtpPassword() {
        return appConfig.getString("smtpPassword");
    }

    public String getSmtpAuth() {
        return appConfig.getString("smtpAuth");
    }

    public String getSmtpStartTlsEnable() {
        return appConfig.getString("smtpStartTlsEnable");
    }

    public String getSmtpSocketFactoryClass() {
        return appConfig.getString("smtpSocketFactoryClass");
    }

    public String getSmtpSocketFactoryFallback() {
        return appConfig.getString("smtpSocketFactoryFallback");
    }

    public String getTestTeamCoord() {
        return appConfig.getString("com.pkb.test.testTeamCoord", "");
    }

    public String getTestClinician() {
        return appConfig.getString("com.pkb.test.testClinician", "az.pro.1@pkbtest.com");
    }

    public String getTestPatient() {
        return appConfig.getString("com.pkb.test.testPatient", "az.patient.1@pkbtest.com");
    }

    public int getLetterInvitationTokenSize() {
        return appConfig.getInt("letter.invitation.token.size", LetterInvitationSettings.DEFAULT_TOKEN_SIZE);
    }

    public int getLetterInvitationAccessCodeSize() {
        return appConfig.getInt("letter.invitation.access.code.size", LetterInvitationSettings.DEFAULT_ACCESS_CODE_SIZE);
    }

    public int getLetterInvitationExpiry() {
        return appConfig.getInt("letter.invitation.token.expiry", LetterInvitationSettings.DEFAULT_EXPIRY_IN_SECONDS);
    }

    public long getUploadMaxFileSize() {
        return appConfig.getLong("com.pkb.upload.maxFileSize", 10000000000L);
    }

    public String getUploadMaxFileText() {
        return appConfig.getString("com.pkb.upload.maxFileText", "10");
    }

    public long getImageUploadMaxFileSize() {
        return appConfig.getLong("com.pkb.upload.maxImageFileSize", 1000000000L);
    }

    public String getEmisEsJobCron() {
        return appConfig.getString("emisEsJobCron");
    }

    public String getSciStoreNotificationCron() {
        return appConfig.getString("sciStoreNotificationCron");
    }

    public String getScriStorePatientSyncCron() {
        return appConfig.getString("sciStorePatientSyncCron");
    }

    public String getChildBirthNotificationCron() {
        return appConfig.getString("childBirthNotificationCron");
    }

    public String getExpiredConsentRemoverCron() {
        return appConfig.getString("expiredConsentRemoverCron");
    }

    public String getSymptomsNotificationCron() {
        return appConfig.getString("symptomsNotificationCron");
    }

    public String getImageUploadMaxFileText() {
        return appConfig.getString("com.pkb.upload.maxImageFileText", "1");
    }

    public long getGeneticsUploadMaxFileSize() {
        return appConfig.getLong("com.pkb.upload.maxGeneticsFileSize", 1000000000L);
    }

    public String getGeneticsUploadMaxFileText() {
        return appConfig.getString("com.pkb.upload.maxGeneticsFileTextGB", "1");
    }

    public long getPlanUploadMaxFileSize() {
        return appConfig.getLong("com.pkb.upload.maxPlanFileSize", 20000000L);
    }

    public String getPlanUploadMaxFileText() {
        return appConfig.getString("com.pkb.upload.maxPlanFileTextMB", "20");
    }

    public boolean isEmisEsEnabled() {
        return appConfig.getBoolean("feature.emis.es.enabled");
    }

    public String getAuthorizationEndpointAddress() {
        return appConfig.getString("authorization.endpoint.address", "http://localhost");
    }

    public String getRestApiClientId() {
        return appConfig.getString("com.pkb.api.util.DefaultInternalAccessTokenIssuer.clientId");
    }

    public String getEMISSSOMonitoringProfEmail() {
        return appConfig.getString("emis.sso.monitoring.prof.email");
    }

    public boolean isTimelineEnabled() {
        return appConfig.getBoolean("timelineEnabled", false);
    }

    public String getTimelineFrontendFetchURL() {
        return appConfig.getString("timelineFrontendFetchURL");
    }

    public String getTimelineFrontendBrowserBaseURL() {
        return appConfig.getString("timelineFrontendBrowserBaseURL");
    }

    public int getTimelineHtmlExpiryInSeconds() {
        return appConfig.getInt("timelineHtmlExpiryInSeconds");
    }

    public String getValidicURL() {
        return appConfig.getString("validicUrl");
    }

    public int getValidicConnectionTimeoutMillis() {
        return appConfig.getInt("validicConnectionTimeoutMillis");
    }

    public int getValidicReadTimeoutMillis() {
        return appConfig.getInt("validicReadTimeoutMillis");
    }

    public long getComposeMessageMaxFileSize() {
        return appConfig.getLong("com.pkb.upload.ComposeMessage.maxFileSize");
    }

    public String getComposeMessageFileSizeText() {
        return appConfig.getString("com.pkb.upload.ComposeMessage.maxFileText");
    }

    public boolean isCSRFProtectionEnabled() {
        return appConfig.getBoolean("csrf.protection.enabled");
    }

    public boolean isTrackingEnabled() {
        return appConfig.getBoolean("trackingEnabled", false);
    }

    public boolean isScistoreEnabled() {
        return appConfig.getBoolean("feature.scistore.enabled", false);
    }

    public int getMenudataQueryBatchSize() {
        return appConfig.getInt("menudataQueryBatchSize", 5000);
    }

    public boolean isClientCachingOfStaticFilesEnabled() {
        return appConfig.getBoolean("clientCachingOfStaticFiles");
    }

    public boolean isDisplayOfSensitiveErrorInformationEnabled() {
        return appConfig.getBoolean("security.display.sensitive.error.info.enabled", false);
    }

    public boolean isSkipHL7IpWhitelistEnabled() {
        return appConfig.getBoolean("security.skip.hl7.whitelist.enabled", false);
    }

    public boolean isTestUsersEnabled() {
        return appConfig.getBoolean("security.test.users.enabled", false);
    }

    public boolean isFhirApiExperimental() {
        return appConfig.getBoolean("fhir.api.experimental");
    }

    public int getEmisEsProcessingBatchSize() {
        return appConfig.getInt("emisEsBatchSize");
    }
}

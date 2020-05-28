package com.pkb.common.config;

import java.nio.file.Path;
import java.util.Optional;

/**
 * This config class allows dynamic injection of values at runtime
 * It's designed to be used when the code is in test, for example to flip feature switches at runtime
 * For production code, use {@link ImmutablePhrConfig}
 */
public class MutablePhrConfig extends BaseMutableConfig implements PhrConfig {

    private PhrConfig defaultConfig;

    public MutablePhrConfig(PhrConfig defaultConfig) {
        super();
        this.defaultConfig = defaultConfig;
    }

    @Override
    public int getMaxBulkInvitationOutcomeReport() {
        return getIntValue(configMap.get("maxBulkInvitationOutcomeReport"))
                .orElseGet(() -> defaultConfig.getMaxBulkInvitationOutcomeReport());
    }

    @Override
    public String getFromEmailAddress() {
        String fromEmailAddress = configMap.get("fromEmailAddress");
        if (fromEmailAddress != null) {
            return fromEmailAddress;
        }
        return defaultConfig.getFromEmailAddress();
    }

    @Override
    public String getFromEmailName() {
        String fromEmailName = configMap.get("fromEmailName");
        if (fromEmailName != null) {
            return fromEmailName;
        }
        return defaultConfig.getFromEmailName();
    }

    @Override
    public int getInboxPageSize() {
        return getIntValue(configMap.get("inboxPageSize"))
                .orElseGet(() -> defaultConfig.getInboxPageSize());
    }

    @Override
    public int getPatientFilesPerPage() {
        return getIntValue(configMap.get("patientFilesPerPage"))
                .orElseGet(() -> defaultConfig.getPatientFilesPerPage());
    }

    @Override
    public int getPatientDiaryEntriesPerPage() {
        return getIntValue(configMap.get("patientDiaryEntriesPerPage"))
                .orElseGet(() -> defaultConfig.getPatientDiaryEntriesPerPage());
    }

    @Override
    public String getPKBLogoURL() {
        String pkbLogoUrl = configMap.get("pkbLogoUrl");
        if (pkbLogoUrl != null) {
            return pkbLogoUrl;
        }
        return defaultConfig.getPKBLogoURL();
    }

    @Override
    public String getTestEmailId() {
        String testEmailId = configMap.get("testEmailId");
        if (testEmailId != null) {
            return testEmailId;
        }
        return defaultConfig.getTestEmailId();
    }

    @Override
    public String getHelpPhone() {
        String helpPhone = configMap.get("helpPhone");
        if (helpPhone != null) {
            return helpPhone;
        }
        return defaultConfig.getHelpPhone();
    }

    @Override
    public String getHelpEmailAddress() {
        String helpEmailAddress = configMap.get("helpEmailAddress");
        if (helpEmailAddress != null) {
            return helpEmailAddress;
        }
        return defaultConfig.getHelpEmailAddress();
    }

    @Override
    public String getHelpWebSite() {
        String helpWebSite = configMap.get("helpWebSite");
        if (helpWebSite != null) {
            return helpWebSite;
        }
        return defaultConfig.getHelpWebSite();
    }

    @Override
    public String getInstituteLogoBaseDir() {
        String instituteLogoBaseDir = configMap.get("instituteLogoBaseDir");
        if (instituteLogoBaseDir != null) {
            return instituteLogoBaseDir;
        }
        return defaultConfig.getInstituteLogoBaseDir();
    }

    @Override
    public String getPHPlanTemplateImageBaseDir() {
        String pHPlanTemplateImageBaseDirectory = configMap.get("pHPlanTemplateImageBaseDirectory");
        if (pHPlanTemplateImageBaseDirectory != null) {
            return pHPlanTemplateImageBaseDirectory;
        }
        return defaultConfig.getPHPlanTemplateImageBaseDir();
    }

    @Override
    public Path getImageUploadLogoBaseDirectory() {
        return getPathValue(configMap.get("imageUploadLogoBaseDir"))
                .orElseGet(() -> defaultConfig.getImageUploadLogoBaseDirectory());
    }

    @Override
    public String getImageUploadPHPlanTemplateImageBaseDirectory() {
        String imageUploadPHPlanTemplateImageBaseDir = configMap.get("imageUploadPHPlanTemplateImageBaseDir");
        if (imageUploadPHPlanTemplateImageBaseDir != null) {
            return imageUploadPHPlanTemplateImageBaseDir;
        }
        return defaultConfig.getImageUploadPHPlanTemplateImageBaseDirectory();
    }

    @Override
    public String getHelpName() {
        String helpName = configMap.get("helpName");
        if (helpName != null) {
            return helpName;
        }
        return defaultConfig.getHelpName();
    }

    @Override
    public int getRecipientColumnSize() {
        return getIntValue(configMap.get("recipientColumnSize"))
                .orElseGet(() -> defaultConfig.getRecipientColumnSize());
    }

    @Override
    public int getThumbnailImageWidth() {
        return getIntValue(configMap.get("thumbnailImageWidth"))
                .orElseGet(() -> defaultConfig.getThumbnailImageWidth());
    }

    @Override
    public int getThumbnailImageHeight() {
        return getIntValue(configMap.get("thumbnailImageHeight"))
                .orElseGet(() -> defaultConfig.getThumbnailImageHeight());
    }

    @Override
    public int getRadiologyReportExtractLength() {
        return getIntValue(configMap.get("radiologyReportExtractLength"))
                .orElseGet(() -> defaultConfig.getRadiologyReportExtractLength());
    }

    @Override
    public String getThumbnailImageFormat() {
        String thumbnailImageFormat = configMap.get("thumbnailImageFormat");
        if (thumbnailImageFormat != null) {
            return thumbnailImageFormat;
        }
        return defaultConfig.getThumbnailImageFormat();
    }

    @Override
    public boolean getSendReminderEmailsEnabled() {
        return getBooleanValue(configMap.get("sendReminderEmailsEnabled"))
                .orElseGet(() -> defaultConfig.getSendReminderEmailsEnabled());
    }

    @Override
    public boolean isUnreadMessageReminderEnabled() {
        return getBooleanValue(configMap.get("sendUnreadMessageReminderEnabled"))
                .orElseGet(() -> defaultConfig.isUnreadMessageReminderEnabled());
    }

    @Override
    public boolean isUnreadDocumentReminderEnabled() {
        return getBooleanValue(configMap.get("unreadDocumentReminderEnabled"))
                .orElseGet(() -> defaultConfig.isUnreadDocumentReminderEnabled());
    }

    @Override
    public String getWithingsOauthKey() {
        String withingsOauthKey = configMap.get("withingsOauthKey");
        if (withingsOauthKey != null) {
            return withingsOauthKey;
        }
        return defaultConfig.getWithingsOauthKey();
    }

    @Override
    public String getWithingsOauthSecret() {
        String withingsOauthSecret = configMap.get("withingsOauthSecret");
        if (withingsOauthSecret != null) {
            return withingsOauthSecret;
        }
        return defaultConfig.getWithingsOauthSecret();
    }

    @Override
    public String getVitaDockOauthKey() {
        String vitaDockOauthKey = configMap.get("vitaDockOauthKey");
        if (vitaDockOauthKey != null) {
            return vitaDockOauthKey;
        }
        return defaultConfig.getVitaDockOauthKey();
    }

    @Override
    public String getVitaDockOauthSecret() {
        String vitaDockOauthSecret = configMap.get("vitaDockOauthSecret");
        if (vitaDockOauthSecret != null) {
            return vitaDockOauthSecret;
        }
        return defaultConfig.getVitaDockOauthSecret();
    }

    @Override
    public String getSciStoreEndpoint() {
        String sciStoreEndpoint = configMap.get("sciStoreEndpoint");
        if (sciStoreEndpoint != null) {
            return sciStoreEndpoint;
        }
        return defaultConfig.getSciStoreEndpoint();
    }

    @Override
    public String getSciStoreUsername() {
        String sciStoreUsername = configMap.get("sciStoreUsername");
        if (sciStoreUsername != null) {
            return sciStoreUsername;
        }
        return defaultConfig.getSciStoreUsername();
    }

    @Override
    public String getSciStorePassword() {
        String sciStorePassword = configMap.get("sciStorePassword");
        if (sciStorePassword != null) {
            return sciStorePassword;
        }
        return defaultConfig.getSciStorePassword();
    }

    @Override
    public String getFakeS3Endpoint() {
        String fakeS3Endpoint = configMap.get("fakeS3Endpoint");
        if (fakeS3Endpoint != null) {
            return fakeS3Endpoint;
        }
        return defaultConfig.getFakeS3Endpoint();
    }

    @Override
    public boolean isDefinedFakeS3Endpoint() {
        return getBooleanValue(configMap.get("definedFakeS3Endpoint"))
                .orElseGet(() -> defaultConfig.isDefinedFakeS3Endpoint());
    }

    @Override
    public String getFakeS3EndpointPublishedURL() {
        String fakeS3EndpointPublishedUrl = configMap.get("fakeS3EndpointPublishedUrl");
        if (fakeS3EndpointPublishedUrl != null) {
            return fakeS3EndpointPublishedUrl;
        }
        return defaultConfig.getFakeS3EndpointPublishedURL();
    }

    @Override
    public int getOrgAdminDashboardPageSize() {
        return getIntValue(configMap.get("adminDashboardPageSize"))
                .orElseGet(() -> defaultConfig.getOrgAdminDashboardPageSize());
    }

    @Override
    public int getAccessLogPageSize() {
        return getIntValue(configMap.get("accessLogPageSize"))
                .orElseGet(() -> defaultConfig.getAccessLogPageSize());
    }

    @Override
    public String getCoreDevicesOrganizationId() {
        String coreDevicesOrganiztionId = configMap.get("coreDevicesOrganiztionId");
        if (coreDevicesOrganiztionId != null) {
            return coreDevicesOrganiztionId;
        }
        return defaultConfig.getCoreDevicesOrganizationId();
    }

    @Override
    public String getCoreDevicesAccessToken() {
        String coreDevicesAccessToken = configMap.get("coreDevicesAccessToken");
        if (coreDevicesAccessToken != null) {
            return coreDevicesAccessToken;
        }
        return defaultConfig.getCoreDevicesAccessToken();
    }

    @Override
    public int getSciStoreNewPatientLimit() {
        return getIntValue(configMap.get("sciStoreNewPatientLimit"))
                .orElseGet(() -> defaultConfig.getSciStoreNewPatientLimit());
    }

    @Override
    public int getLatestResultsInitialThreshold() {
        return getIntValue(configMap.get("latestResultsInitialThreshold"))
                .orElseGet(() -> defaultConfig.getLatestResultsInitialThreshold());
    }

    @Override
    public int getHighchartsThreshold() {
        return getIntValue(configMap.get("highchartsThreshold"))
                .orElseGet(() -> defaultConfig.getHighchartsThreshold());
    }

    @Override
    public boolean isUseUTCInHighcharts() {
        return getBooleanValue(configMap.get("useUTCInHighcharts"))
                .orElseGet(() -> defaultConfig.isUseUTCInHighcharts());
    }

    @Override
    public boolean isSortEmisCsvs() {
        return getBooleanValue(configMap.get("sortEmisCsvs"))
                .orElseGet(() -> defaultConfig.isSortEmisCsvs());
    }

    @Override
    public String getLabtestsonlineSearchURL() {
        String labTestsOnlineSearchURL = configMap.get("labTestsOnlineSearchURL");
        if (labTestsOnlineSearchURL != null) {
            return labTestsOnlineSearchURL;
        }
        return defaultConfig.getLabtestsonlineSearchURL();
    }

    @Override
    public String getLabtestsonlineRefURL() {
        String labTestsOnlineRefURL = configMap.get("labTestsOnlineRefURL");
        if (labTestsOnlineRefURL != null) {
            return labTestsOnlineRefURL;
        }
        return defaultConfig.getLabtestsonlineRefURL();
    }

    @Override
    public String getHelpTextURL() {
        String helpTextURL = configMap.get("helpTextURL");
        if (helpTextURL != null) {
            return helpTextURL;
        }
        return defaultConfig.getHelpTextURL();
    }

    @Override
    public int getFileChunkSizeInBytes() {
        return getIntValue(configMap.get("fileChunkSizeInBytes"))
                .orElseGet(() -> defaultConfig.getFileChunkSizeInBytes());
    }

    @Override
    public int getDocDeleteBatchSize() {
        return getIntValue(configMap.get("docDeleteBatchSize"))
                .orElseGet(() -> defaultConfig.getDocDeleteBatchSize());
    }

    @Override
    public long getPatientBannerErrorTimeoutMillis() {
        return getLongValue(configMap.get("patientBannerErrorTimeoutMillis"))
                .orElseGet(() -> defaultConfig.getPatientBannerErrorTimeoutMillis());
    }

    @Override
    public int getDocDeleteIntervalInHours() {
        return getIntValue(configMap.get("docDeleteIntervalInHours"))
                .orElseGet(() -> defaultConfig.getDocDeleteIntervalInHours());
    }

    @Override
    public Boolean isPrometheusReportingEnabled() {
        return getBooleanValue(configMap.get("prometheusReportingEnabled"))
                .orElseGet(() -> defaultConfig.isPrometheusReportingEnabled());
    }

    @Override
    public String getLibraryUploadDirectory() {
        String libraryUploadDirectory = configMap.get("libraryUploadDirectory");
        if (libraryUploadDirectory != null) {
            return libraryUploadDirectory;
        }
        return defaultConfig.getLibraryUploadDirectory();
    }

    @Override
    public String getNonCryptoAccessKey() {
        String nonCryptoAccessKey = configMap.get("nonCryptoAccessKey");
        if (nonCryptoAccessKey != null) {
            return nonCryptoAccessKey;
        }
        return defaultConfig.getNonCryptoAccessKey();
    }

    @Override
    public String getNonCryptoSecretKey() {
        String nonCryptoSecretKey = configMap.get("nonCryptoSecretKey");
        if (nonCryptoSecretKey != null) {
            return nonCryptoSecretKey;
        }
        return defaultConfig.getNonCryptoSecretKey();
    }

    @Override
    public String getNonCryptoBucket() {
        String nonCryptoBucket = configMap.get("nonCryptoBucket");
        if (nonCryptoBucket != null) {
            return nonCryptoBucket;
        }
        return defaultConfig.getNonCryptoBucket();
    }

    @Override
    public String getProxyHost() {
        String proxyHost = configMap.get("proxyHost");
        if (proxyHost != null) {
            return proxyHost;
        }
        return defaultConfig.getProxyHost();
    }

    @Override
    public int getProxyPort() {
        return getIntValue(configMap.get("proxyPort"))
                .orElseGet(() -> defaultConfig.getProxyPort());
    }

    @Override
    public String getEmisEsDownloadBaseDir() {
        String emisEsDownloadBaseDir = configMap.get("emisEsDownloadBaseDir");
        if (emisEsDownloadBaseDir != null) {
            return emisEsDownloadBaseDir;
        }
        return defaultConfig.getEmisEsDownloadBaseDir();
    }

    @Override
    public String getEmisEsPgpPrivateKeyFile() {
        String emisEsPgpPrivateKeyFile = configMap.get("emisEsPgpPrivateKeyFile");
        if (emisEsPgpPrivateKeyFile != null) {
            return emisEsPgpPrivateKeyFile;
        }
        return defaultConfig.getEmisEsPgpPrivateKeyFile();
    }

    @Override
    public String getEmisEsPgpPrivateKeyFilePassphrase() {
        String emisEsPrivateKeyFilePassphrase = configMap.get("emisEsPrivateKeyFilePassphrase");
        if (emisEsPrivateKeyFilePassphrase != null) {
            return emisEsPrivateKeyFilePassphrase;
        }
        return defaultConfig.getEmisEsPgpPrivateKeyFilePassphrase();
    }

    @Override
    public String getEmisEsCsvEncoding() {
        String emisEsCsvEncoding = configMap.get("emisEsCsvEncoding");
        if (emisEsCsvEncoding != null) {
            return emisEsCsvEncoding;
        }
        return defaultConfig.getEmisEsCsvEncoding();
    }

    @Override
    public Boolean isEmisEsWhiteListEnabled() {
        return getBooleanValue(configMap.get("emisEsWhiteListEnabled"))
                .orElseGet(() -> defaultConfig.isEmisEsWhiteListEnabled());
    }

    @Override
    public int getEmisFailureGracePeriodHours() {
        return getIntValue(configMap.get("emisFailureGracePeriodHours"))
                .orElseGet(() -> defaultConfig.getEmisFailureGracePeriodHours());
    }

    @Override
    public Path getEmisEsSftpConfigYamlPath() {
        return getPathValue(configMap.get("emisEsFtpConfigYamlPath"))
                .orElseGet(() -> defaultConfig.getEmisEsSftpConfigYamlPath());
    }

    @Override
    public Optional<Path> getEmisEsWhitelistYamlPath() {
        if (configMap.containsKey("emisEsWhitelistYamlPath")) {
            return getPathValue(configMap.get("emisEsWhitelistYamlPath"));
        }
        return defaultConfig.getEmisEsWhitelistYamlPath();
    }

    @Override
    public boolean isUseProxy() {
        return getBooleanValue(configMap.get("useProxy"))
                .orElseGet(() -> defaultConfig.isUseProxy());
    }

    @Override
    public boolean isProxyConfigured() {
        return getBooleanValue(configMap.get("proxyConfigured"))
                .orElseGet(() -> defaultConfig.isProxyConfigured());
    }

    @Override
    public boolean isUseHttpForS3() {
        return getBooleanValue(configMap.get("useHttpForS3"))
                .orElseGet(() -> defaultConfig.isUseHttpForS3());
    }

    @Override
    public String getSmtpHostname() {
        String smtpHostname = configMap.get("smtpHostname");
        if (smtpHostname != null) {
            return smtpHostname;
        }
        return defaultConfig.getSmtpHostname();
    }

    @Override
    public int getSmtpPort() {
        return getIntValue(configMap.get("smtpPort"))
                .orElseGet(() -> defaultConfig.getSmtpPort());
    }

    @Override
    public String getSmtpUser() {
        String smtpUser = configMap.get("smtpUser");
        if (smtpUser != null) {
            return smtpUser;
        }
        return defaultConfig.getSmtpUser();
    }

    @Override
    public String getSmtpPassword() {
        String smtpPassword = configMap.get("smtpPassword");
        if (smtpPassword != null) {
            return smtpPassword;
        }
        return defaultConfig.getSmtpPassword();
    }

    @Override
    public String getSmtpAuth() {
        String smtpAuth = configMap.get("smtpAuth");
        if (smtpAuth != null) {
            return smtpAuth;
        }
        return defaultConfig.getSmtpAuth();
    }

    @Override
    public String getSmtpStartTlsEnable() {
        String smtpStartTlsEnable = configMap.get("smtpStartTlsEnable");
        if (smtpStartTlsEnable != null) {
            return smtpStartTlsEnable;
        }
        return defaultConfig.getSmtpStartTlsEnable();
    }

    @Override
    public String getSmtpSocketFactoryClass() {
        String smtpSocketFactoryClass = configMap.get("smtpSocketFactoryClass");
        if (smtpSocketFactoryClass != null) {
            return smtpSocketFactoryClass;
        }
        return defaultConfig.getSmtpSocketFactoryClass();
    }

    @Override
    public String getSmtpSocketFactoryFallback() {
        String smtpSocketFactoryFallback = configMap.get("smtpSocketFactoryFallback");
        if (smtpSocketFactoryFallback != null) {
            return smtpSocketFactoryFallback;
        }
        return defaultConfig.getSmtpSocketFactoryFallback();
    }

    @Override
    public String getTestTeamCoord() {
        String testTeamCoord = configMap.get("testTeamCoord");
        if (testTeamCoord != null) {
            return testTeamCoord;
        }
        return defaultConfig.getTestTeamCoord();
    }

    @Override
    public String getTestClinician() {
        String testClinician = configMap.get("testClinician");
        if (testClinician != null) {
            return testClinician;
        }
        return defaultConfig.getTestClinician();
    }

    @Override
    public String getTestPatient() {
        String testPatient = configMap.get("testPatient");
        if (testPatient != null) {
            return testPatient;
        }
        return defaultConfig.getTestPatient();
    }

    @Override
    public int getLetterInvitationTokenSize() {
        return getIntValue(configMap.get("letterInvitationTokenSize"))
                .orElseGet(() -> defaultConfig.getLetterInvitationTokenSize());
    }

    @Override
    public int getLetterInvitationAccessCodeSize() {
        return getIntValue(configMap.get("letterInvitationAccessCodeSize"))
                .orElseGet(() -> defaultConfig.getLetterInvitationAccessCodeSize());
    }

    @Override
    public int getLetterInvitationExpiry() {
        return getIntValue(configMap.get("letterInvitationExpiry"))
                .orElseGet(() -> defaultConfig.getLetterInvitationExpiry());
    }

    @Override
    public long getUploadMaxFileSize() {
        return getLongValue(configMap.get("uploadMaxFileSize"))
                .orElseGet(() -> defaultConfig.getUploadMaxFileSize());
    }

    @Override
    public String getUploadMaxFileText() {
        String uploadMaxFileText = configMap.get("uploadMaxFileText");
        if (uploadMaxFileText != null) {
            return uploadMaxFileText;
        }
        return defaultConfig.getUploadMaxFileText();
    }

    @Override
    public long getImageUploadMaxFileSize() {
        return getLongValue(configMap.get("imageUploadMaxFileSize"))
                .orElseGet(() -> defaultConfig.getImageUploadMaxFileSize());
    }

    @Override
    public String getEmisEsJobCron() {
        String emisEsJobCron = configMap.get("emisEsJobCron");
        if (emisEsJobCron != null) {
            return emisEsJobCron;
        }
        return defaultConfig.getEmisEsJobCron();
    }

    @Override
    public String getSciStoreNotificationCron() {
        String sciStoreNotificationCron = configMap.get("sciStoreNotificationCron");
        if (sciStoreNotificationCron != null) {
            return sciStoreNotificationCron;
        }
        return defaultConfig.getSciStoreNotificationCron();
    }

    @Override
    public String getScriStorePatientSyncCron() {
        String sciStorePatientSyncCron = configMap.get("sciStorePatientSyncCron");
        if (sciStorePatientSyncCron != null) {
            return sciStorePatientSyncCron;
        }
        return defaultConfig.getScriStorePatientSyncCron();
    }

    @Override
    public String getChildBirthNotificationCron() {
        String childBirthNotificationCron = configMap.get("childBirthNotificationCron");
        if (childBirthNotificationCron != null) {
            return childBirthNotificationCron;
        }
        return defaultConfig.getChildBirthNotificationCron();
    }

    @Override
    public String getExpiredConsentRemoverCron() {
        String expiredConsentRemoverCron = configMap.get("expiredConsentRemoverCron");
        if (expiredConsentRemoverCron != null) {
            return expiredConsentRemoverCron;
        }
        return defaultConfig.getExpiredConsentRemoverCron();
    }

    @Override
    public String getSymptomsNotificationCron() {
        String symptomsNotificationCron = configMap.get("symptomsNotificationCron");
        if (symptomsNotificationCron != null) {
            return symptomsNotificationCron;
        }
        return defaultConfig.getSymptomsNotificationCron();
    }

    @Override
    public String getUnreadNotificationCron() {
        String unreadNotificationCron = configMap.get("unreadNotificationCron");
        if (unreadNotificationCron != null) {
            return unreadNotificationCron;
        }
        return defaultConfig.getUnreadNotificationCron();
    }

    @Override
    public String getImageUploadMaxFileText() {
        String imageUploadMaxFileText = configMap.get("imageUploadMaxFileText");
        if (imageUploadMaxFileText != null) {
            return imageUploadMaxFileText;
        }
        return defaultConfig.getImageUploadMaxFileText();
    }

    @Override
    public long getGeneticsUploadMaxFileSize() {
        return getLongValue(configMap.get("geneticsUploadMaxFileSize"))
                .orElseGet(() -> defaultConfig.getGeneticsUploadMaxFileSize());
    }

    @Override
    public String getGeneticsUploadMaxFileText() {
        String geneticsUploadMaxFileText = configMap.get("geneticsUploadMaxFileText");
        if (geneticsUploadMaxFileText != null) {
            return geneticsUploadMaxFileText;
        }
        return defaultConfig.getGeneticsUploadMaxFileText();
    }

    @Override
    public long getPlanUploadMaxFileSize() {
        return getLongValue(configMap.get("planUploadMaxFileSize"))
                .orElseGet(() -> defaultConfig.getPlanUploadMaxFileSize());
    }

    @Override
    public String getPlanUploadMaxFileText() {
        String planUploadMaxFileText = configMap.get("planUploadMaxFileText");
        if (planUploadMaxFileText != null) {
            return planUploadMaxFileText;
        }
        return defaultConfig.getPlanUploadMaxFileText();
    }

    @Override
    public boolean isEmisEsEnabled() {
        return getBooleanValue(configMap.get("emisEsEnabled"))
                .orElseGet(() -> defaultConfig.isEmisEsEnabled());
    }

    @Override
    public boolean isBulkDisableSharingEnabled() {
        return getBooleanValue(configMap.get("bulkDisableSharingEnabled"))
                .orElseGet(() -> defaultConfig.isBulkDisableSharingEnabled());
    }

    @Override
    public String getAuthorizationEndpointAddress() {
        String authorizationEndpointAddress = configMap.get("authorizationEndpointAddress");
        if (authorizationEndpointAddress != null) {
            return authorizationEndpointAddress;
        }
        return defaultConfig.getAuthorizationEndpointAddress();
    }

    @Override
    public String getRestApiClientId() {
        String restApiClientId = configMap.get("restApiClientId");
        if (restApiClientId != null) {
            return restApiClientId;
        }
        return defaultConfig.getRestApiClientId();
    }

    @Override
    public String getEMISSSOMonitoringProfEmail() {
        String emisSsoMonitoringProfEmail = configMap.get("emisSsoMonitoringProfEmail");
        if (emisSsoMonitoringProfEmail != null) {
            return emisSsoMonitoringProfEmail;
        }
        return defaultConfig.getEMISSSOMonitoringProfEmail();
    }

    @Override
    public boolean isRollBarEnabled() {
        return getBooleanValue(configMap.get("rollBarEnabled"))
                .orElseGet(() -> defaultConfig.isRollBarEnabled());
    }

    @Override
    public Optional<String> getRollBarEnvironment() {
        if (configMap.containsKey("rollBarEnvironment")) {
            String overrideValue = configMap.get("rollBarEnvironment");
            if (overrideValue != null) {
                return Optional.of(overrideValue);
            }
            return Optional.empty();
        }
        return defaultConfig.getRollBarEnvironment();
    }

    @Override
    public Optional<String> getRollbarEndpoint() {
        if (configMap.containsKey("rollBarEndpoint")) {
            String overrideValue = configMap.get("rollBarEndpoint");
            if (overrideValue != null) {
                return Optional.of(overrideValue);
            }
            return Optional.empty();
        }
        return defaultConfig.getRollbarEndpoint();
    }

    @Override
    public boolean isTimelineEnabled() {
        return getBooleanValue(configMap.get("timelineEnabled"))
                .orElseGet(() -> defaultConfig.isTimelineEnabled());
    }

    @Override
    public boolean isTimelineCalendarEnabled() {
        return getBooleanValue(configMap.get("timelineCalendarEnabled"))
                .orElseGet(() -> defaultConfig.isTimelineCalendarEnabled());
    }

    @Override
    public boolean isTimelineTestsEnabled() {
        return getBooleanValue(configMap.get("timelineTestsEnabled"))
                .orElseGet(() -> defaultConfig.isTimelineTestsEnabled());
    }

    @Override
    public boolean isTimelineRadiologyEnabled() {
        return getBooleanValue(configMap.get("timelineRadiologyEnabled"))
                .orElseGet(() -> defaultConfig.isTimelineRadiologyEnabled());
    }

    @Override
    public boolean isTimelineSymptomsEnabled() {
        return getBooleanValue(configMap.get("timelineSymptomsEnabled"))
                .orElseGet(() -> defaultConfig.isTimelineSymptomsEnabled());
    }

    @Override
    public boolean isTimelineMeasurementsEnabled() {
        return getBooleanValue(configMap.get("timelineMeasurementsEnabled"))
                .orElseGet(() -> defaultConfig.isTimelineMeasurementsEnabled());
    }

    @Override
    public boolean isTimelinePreloadingEnabled() {
        return getBooleanValue(configMap.get("timelinePreloadingEnabled"))
                .orElseGet(() -> defaultConfig.isTimelinePreloadingEnabled());
    }

    @Override
    public int getTimelineRequestTimeout() {
        return getIntValue(configMap.get("timelineRequestTimeout"))
                .orElseGet(() -> defaultConfig.getTimelineRequestTimeout());
    }

    @Override
    public boolean isPatientBannerEnabled() {
        return getBooleanValue(configMap.get("patientBannerEnabled"))
                .orElseGet(() -> defaultConfig.isPatientBannerEnabled());
    }

    @Override
    public boolean isRejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3() {
        return getBooleanValue(configMap.get("rejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3"))
                .orElseGet(() -> defaultConfig.isRejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3());
    }

    @Override
    public String getTimelineFrontendFetchURL() {
        String timelineFrontendFetchURL = configMap.get("timelineFrontendFetchURL");
        if (timelineFrontendFetchURL != null) {
            return timelineFrontendFetchURL;
        }
        return defaultConfig.getTimelineFrontendFetchURL();
    }

    @Override
    public String getTimelineFrontendBrowserBaseURL() {
        String timelineFrontendBrowserBaseURL = configMap.get("timelineFrontendBrowserBaseURL");
        if (timelineFrontendBrowserBaseURL != null) {
            return timelineFrontendBrowserBaseURL;
        }
        return defaultConfig.getTimelineFrontendBrowserBaseURL();
    }

    @Override
    public int getTimelineHtmlExpiryInSeconds() {
        return getIntValue(configMap.get("timelineHtmlExpiryInSeconds"))
                .orElseGet(() -> defaultConfig.getTimelineHtmlExpiryInSeconds());
    }

    @Override
    public String getValidicURL() {
        String validicURL = configMap.get("validicURL");
        if (validicURL != null) {
            return validicURL;
        }
        return defaultConfig.getValidicURL();
    }

    @Override
    public int getValidicConnectionTimeoutMillis() {
        return getIntValue(configMap.get("validicConnectionTimeoutMillis"))
                .orElseGet(() -> defaultConfig.getValidicConnectionTimeoutMillis());
    }

    @Override
    public int getValidicReadTimeoutMillis() {
        return getIntValue(configMap.get("validicReadTimeoutMillis"))
                .orElseGet(() -> defaultConfig.getValidicReadTimeoutMillis());
    }

    @Override
    public long getComposeMessageMaxFileSize() {
        return getLongValue(configMap.get("composeMessageMaxFileSize"))
                .orElseGet(() -> defaultConfig.getComposeMessageMaxFileSize());
    }

    @Override
    public String getComposeMessageFileSizeText() {
        String composeMessageFileSizeText = configMap.get("composeMessageFileSizeText");
        if (composeMessageFileSizeText != null) {
            return composeMessageFileSizeText;
        }
        return defaultConfig.getComposeMessageFileSizeText();
    }

    @Override
    public boolean isCSRFProtectionEnabled() {
        return getBooleanValue(configMap.get("csrfProtectionEnabled"))
                .orElseGet(() -> defaultConfig.isCSRFProtectionEnabled());
    }

    @Override
    public boolean isTrackingEnabled() {
        return getBooleanValue(configMap.get("trackingEnabled"))
                .orElseGet(() -> defaultConfig.isTrackingEnabled());
    }

    @Override
    public int getMatomoSiteId() {
        return getIntValue(configMap.get("matomoSiteId"))
                .orElseGet(() -> defaultConfig.getMatomoSiteId());
    }

    @Override
    public boolean isScistoreEnabled() {
        return getBooleanValue(configMap.get("sciStoreEnabled"))
                .orElseGet(() -> defaultConfig.isScistoreEnabled());
    }

    @Override
    public int getDBQueryBatchSize() {
        return getIntValue(configMap.get("queryBatchSize"))
                .orElseGet(() -> defaultConfig.getDBQueryBatchSize());
    }

    @Override
    public boolean isClientCachingOfStaticFilesEnabled() {
        return getBooleanValue(configMap.get("clientCachingOfStaticFilesEnabled"))
                .orElseGet(() -> defaultConfig.isClientCachingOfStaticFilesEnabled());
    }

    @Override
    public boolean isDisplayOfSensitiveErrorInformationEnabled() {
        return getBooleanValue(configMap.get("displayOfSensitiveErrorInformationEnabled"))
                .orElseGet(() -> defaultConfig.isDisplayOfSensitiveErrorInformationEnabled());
    }

    @Override
    public boolean isHL7IpWhitelistEnabled() {
        return getBooleanValue(configMap.get("hl7IpWhitelistEnabled"))
                .orElseGet(() -> defaultConfig.isHL7IpWhitelistEnabled());
    }

    @Override
    public boolean isTestUsersEnabled() {
        return getBooleanValue(configMap.get("testUsersEnabled"))
                .orElseGet(() -> defaultConfig.isTestUsersEnabled());
    }

    @Override
    public int getHl7QryA19PageSize() {
        return getIntValue(configMap.get("hl7QryA19PageSize"))
                .orElseGet(() -> defaultConfig.getHl7QryA19PageSize());
    }

    @Override
    public boolean isHL7MdmT11DocumentDeletionEnabled() {
        return getBooleanValue(configMap.get("hl7MdmT11DocumentDeletionEnabled"))
                .orElseGet(() -> defaultConfig.isHL7MdmT11DocumentDeletionEnabled());
    }

    @Override
    public boolean isHL7MdmT01DocumentUpdateEnabled() {
        return getBooleanValue(configMap.get("hl7MdmT01DocumentUpdateEnabled"))
                .orElseGet(() -> defaultConfig.isHL7MdmT01DocumentUpdateEnabled());
    }

    @Override
    public int getNumberOfRecentLoginAttemptsAllowed() {
        return getIntValue(configMap.get("numberOfRecentLoginAttemptsAllowed"))
                .orElseGet(() -> defaultConfig.getNumberOfRecentLoginAttemptsAllowed());
    }

    @Override
    public int getDefinitionOfRecentLoginAttemptInSeconds() {
        return getIntValue(configMap.get("definitionOfRecentLoginAttemptInSeconds"))
                .orElseGet(() -> defaultConfig.getDefinitionOfRecentLoginAttemptInSeconds());
    }

    @Override
    public int getEmisEsProcessingBatchSize() {
        return getIntValue(configMap.get("emisEsProcessingBatchSize"))
                .orElseGet(() -> defaultConfig.getEmisEsProcessingBatchSize());
    }

    @Override
    public int getEmisEsCodingBatchSize() {
        return getIntValue(configMap.get("emisEsCodingBatchSize"))
                .orElseGet(() -> defaultConfig.getEmisEsCodingBatchSize());
    }

    @Override
    public boolean isEmisEsStateResumeEnabled() {
        return getBooleanValue(configMap.get("emisEsStateResumeEnabled"))
                .orElseGet(() -> defaultConfig.isEmisEsStateResumeEnabled());
    }

    @Override
    public int getAutosaveTimeoutInMilliseconds() {
        return getIntValue(configMap.get("autosaveTimeoutInMilliseonds"))
                .orElseGet(() -> defaultConfig.getAutosaveTimeoutInMilliseconds());
    }

    @Override
    public int getAppointmentsCalendarMonths() {
        return getIntValue(configMap.get("appointmentsCalendarMonths"))
                .orElseGet(() -> defaultConfig.getAppointmentsCalendarMonths());
    }

    @Override
    public Optional<String> clamAvHost() {
        if (configMap.containsKey("clamAvHost")) {
            String overrideValue = configMap.get("clamAvHost");
            if (overrideValue != null) {
                return Optional.of(overrideValue);
            }
            return Optional.empty();
        }
        return defaultConfig.clamAvHost();
    }

    @Override
    public boolean isFakeHelpPageBaseUrlProviderEnabled() {
        return getBooleanValue(configMap.get("fakeHelpPageBaseUrlProviderEnabled"))
                .orElseGet(() -> defaultConfig.isFakeHelpPageBaseUrlProviderEnabled());
    }

    @Override
    public int getEncounterTimelineRangeMonths() {
        return getIntValue(configMap.get("encounterTimelineRangeMonths"))
                .orElseGet(() -> defaultConfig.getEncounterTimelineRangeMonths());
    }

    @Override
    public int getUserAgentAnalyzerCacheSize() {
        return getIntValue(configMap.get("userAgentAnalyzerCacheSize"))
                .orElseGet(() -> defaultConfig.getUserAgentAnalyzerCacheSize());
    }

    @Override
    public boolean isEnforceSecureCookies() {
        return getBooleanValue(configMap.get("enforceSecureCookies"))
                .orElseGet(() -> defaultConfig.isEnforceSecureCookies());
    }

    @Override
    public String getHospitalMapIframeSrc() {
        String hospitalMapIframeSrc = configMap.get("hospitalMapIframeSrc");
        if (hospitalMapIframeSrc != null) {
            return hospitalMapIframeSrc;
        }
        return defaultConfig.getHospitalMapIframeSrc();
    }

    @Override
    public int orgNetworkSyncTransactionTimeout() {
        return getIntValue(configMap.get("orgNetworkSyncTransactionTimeout"))
                .orElseGet(() -> defaultConfig.orgNetworkSyncTransactionTimeout());
    }

    @Override
    public String getEmisRestUrl() {
        String emisRestUrl = configMap.get("emisRestUrl");
        if (emisRestUrl != null) {
            return emisRestUrl;
        }
        return defaultConfig.getEmisRestUrl();
    }

    @Override
    public String getEmisRestXapiApplicationId() {
        String emisRestXapiApplicationId = configMap.get("emisRestXapiApplicationId");
        if (emisRestXapiApplicationId != null) {
            return emisRestXapiApplicationId;
        }
        return defaultConfig.getEmisRestXapiApplicationId();
    }

    @Override
    public String getRestEmisXapiVersion() {
        String restEmisXapiVersion = configMap.get("restEmisXapiVersion");
        if (restEmisXapiVersion != null) {
            return restEmisXapiVersion;
        }
        return defaultConfig.getRestEmisXapiVersion();
    }

    @Override
    public int getEmisRestConnectTimeout() {
        return getIntValue(configMap.get("emisRestConnectTimeout"))
                .orElseGet(() -> defaultConfig.getEmisRestConnectTimeout());
    }

    @Override
    public String getEmisRestKeystore() {
        String emisRestKeystore = configMap.get("emisRestKeystore");
        if (emisRestKeystore != null) {
            return emisRestKeystore;
        }
        return defaultConfig.getEmisRestKeystore();
    }

    @Override
    public String getOdsRestUrl() {
        String odsRestUrl = configMap.get("odsRestUrl");
        if (odsRestUrl != null) {
            return odsRestUrl;
        }
        return defaultConfig.getOdsRestUrl();
    }

    @Override
    public int getOdsCacheSize() {
        return getIntValue(configMap.get("odsCacheSize"))
                .orElseGet(() -> defaultConfig.getOdsCacheSize());
    }

    @Override
    public int getOdsCacheExpiryHours() {
        return getIntValue(configMap.get("odsCacheExpiryHours"))
                .orElseGet(() -> defaultConfig.getOdsCacheExpiryHours());
    }

    @Override
    public String getMatomoBaseUrl() {
        String matomoBaseUrl = configMap.get("matomoBaseUrl");
        if (matomoBaseUrl != null) {
            return matomoBaseUrl;
        }
        return defaultConfig.getMatomoBaseUrl();
    }

    @Override
    public int getUnregisteredPatientsPageSize() {
        return getIntValue(configMap.get("unregisteredPatientsPageSize"))
                .orElseGet(() -> defaultConfig.getUnregisteredPatientsPageSize());
    }

    @Override
    public int getUploadedDataBatchSyncSize() {
        return getIntValue(configMap.get("uploadedDataBatchSyncSize"))
                .orElseGet(() -> defaultConfig.getUploadedDataBatchSyncSize());
    }

    @Override
    public boolean isNhsLoginWebUiButtonEnabled() {
        return getBooleanValue(configMap.get("nhsLoginWebUiButtonEnabled"))
                .orElseGet(() -> defaultConfig.isNhsLoginWebUiButtonEnabled());
    }

    @Override
    public String getNhsIntegrationUrl() {
        String nhsIntegrationUrl = configMap.get("nhsIntegrationUrl");
        if (nhsIntegrationUrl != null) {
            return nhsIntegrationUrl;
        }
        return defaultConfig.getNhsIntegrationUrl();
    }

    @Override
    public boolean isExceptionForMissingPatientParticipantEnabled() {
        return getBooleanValue(configMap.get("exceptionForMissingPatientParticipant"))
                .orElseGet(() -> defaultConfig.isExceptionForMissingPatientParticipantEnabled());
    }

    @Override
    public boolean isGpAppointmentBookingEnabled() {
        return getBooleanValue(configMap.get("gpApptBookingEnabled"))
                .orElseGet(() -> defaultConfig.isGpAppointmentBookingEnabled());
    }

    @Override
    public boolean isBookAppointmentWarningVisible() {
        return getBooleanValue(configMap.get("bookApptWarningVisible"))
                .orElseGet(() -> defaultConfig.isBookAppointmentWarningVisible());
    }

    @Override
    public boolean isNhsLoginEnabled() {
        return getBooleanValue(configMap.get("nhsLoginEnabled"))
                .orElseGet(() -> defaultConfig.isNhsLoginEnabled());
    }

    @Override
    public boolean isGpPrescriptionOrderingEnabled() {
        return getBooleanValue(configMap.get("gpPrescriptionOrderingEnabled"))
                .orElseGet(() -> defaultConfig.isGpPrescriptionOrderingEnabled());
    }

    @Override
    public boolean isSystmOneSsoEnabled() {
        return getBooleanValue(configMap.get("systmOneSsoEnabled"))
                .orElseGet(() -> defaultConfig.isSystmOneSsoEnabled());
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
        return defaultConfig.getPulsarDefaultNamespce();
    }

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
    public boolean isConversationAssignEnabled() {
        return getBooleanValue(configMap.get("conversationAssignEnabled"))
                .orElseGet(() -> defaultConfig.isConversationAssignEnabled());
    }

    @Override
    public boolean isConversationArchiveEnabled() {
        return getBooleanValue("conversationArchiveEnabled")
                .orElseGet(() -> defaultConfig.isConversationArchiveEnabled());
    }

    @Override
    public int getMassConsultationBatchSize() {
        return getIntValue(configMap.get("massConsultationBatchSize"))
                .orElseGet(() -> defaultConfig.getMaxBulkInvitationOutcomeReport());
    }

    @Override
    public int getMeasurementHistoryDefaultFetchSize() {
        return getIntValue(configMap.get("measurementHistoryDefaultFetchSize"))
                .orElseGet(() -> defaultConfig.getMeasurementHistoryDefaultFetchSize());
    }

    @Override
    public int getMyMeasurementsDefaultFetchSize() {
        return getIntValue(configMap.get("myMeasurementsDefaultFetchSize"))
                .orElseGet(() -> defaultConfig.getMyMeasurementsDefaultFetchSize());
    }

    @Override
    public String getBulkDisableSharingPatientIds() {
        String result = configMap.get("feature.bulkdisablesharing.patientIds");
        if (result == null) {
            result = defaultConfig.getBulkDisableSharingPatientIds();
        }
        return result;
    }

    @Override
    public String asyncSurveyResultsSetting() {
        return configMap.getOrDefault("asyncSurveyResultsSetting", defaultConfig.asyncSurveyResultsSetting());
    }

    @Override
    public String asyncSurveyResultsAppUrl() {
        return configMap.getOrDefault("asyncSurveyResultsAppUrl", defaultConfig.asyncSurveyResultsAppUrl());
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
    BaseConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public boolean isAllowSsoTestingPages() {
        return getBooleanValue(configMap.get("allowSsoTestingPages"))
                .orElseGet(() -> defaultConfig.isAllowSsoTestingPages());
    }
}

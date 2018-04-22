package com.pkb.common.config;

import java.util.Set;

/**
 * Proxy class of {@link ConfigV1} to make code easier to test.
 */
public class ConfigV2 {

    private final static ConfigV1 config = ConfigV1.getConfig();

    public String getBaseURL() {
        return config.getBaseURL();
    }

    public String getExchangeHostName() {
        return config.getExchangeHostName();
    }

    public String getIMAPUserId() {
        return config.getIMAPUserId();
    }

    public String getIMAPPassword() {
        return config.getIMAPPassword();
    }

    public boolean isIMAPSSLEnabled() {
        return config.isIMAPSSLEnabled();
    }

    public String getIncomingMailProtocol() {
        return config.getIncomingMailProtocol();
    }

    public int getIncomingMailPort() {
        return config.getIncomingMailPort();
    }

    public String getFromEmailAddress() {
        return config.getFromEmailAddress();
    }

    public String getFromEmailName() {
        return config.getFromEmailName();
    }

    public String getRecaptchaPrivateKey() {
        return config.getRecaptchaPrivateKey();
    }

    public String getRecaptchaPublicKey() {
        return config.getRecaptchaPublicKey();
    }

    public int getInboxPageSize() {
        return config.getInboxPageSize();
    }

    public int getPatientFilesPerPage() {
        return config.getPatientFilesPerPage();
    }

    public int getPatientDiaryEntriesPerPage() {
        return config.getPatientDiaryEntriesPerPage();
    }

    public String getPKBLogoURL() {
        return config.getPKBLogoURL();
    }

    public String getTestEmailId() {
        return config.getTestEmailId();
    }

    public String getHelpPhone() {
        return config.getHelpPhone();
    }

    public String getHelpEmailAddress() {
        return config.getHelpEmailAddress();
    }

    public String getHelpWebSite() {
        return config.getHelpWebSite();
    }

    public String getInstituteLogoBaseDir() {
        return config.getInstituteLogoBaseDir();
    }

    public String getPHPlanTemplateImageBaseDir() {
        return config.getPHPlanTemplateImageBaseDir();
    }

    public String getImageUploadLogoBaseDirectory() {
        return config.getImageUploadLogoBaseDirectory();
    }

    public String getImageUploadPHPlanTemplateImageBaseDirectory() {
        return config.getImageUploadPHPlanTemplateImageBaseDirectory();
    }

    public String getSftpBaseDir() {
        return config.getSftpBaseDir();
    }

    public String getHelpName() {
        return config.getHelpName();
    }

    public int getRecipientColumnSize() {
        return config.getRecipientColumnSize();
    }

    public int getThumbnailImageWidth() {
        return config.getThumbnailImageWidth();
    }

    public int getThumbnailImageHeight() {
        return config.getThumbnailImageHeight();
    }

    public int getRadiologyReportExtractLength() {
        return config.getRadiologyReportExtractLength();
    }

    public String getThumbnailImageFormat() {
        return config.getThumbnailImageFormat();
    }

    public boolean getSendReminderEmailsEnabled() {
        return config.getSendReminderEmailsEnabled();
    }

    public String getWithingsOauthKey() {
        return config.getWithingsOauthKey();
    }

    public String getWithingsOauthSecret() {
        return config.getWithingsOauthSecret();
    }

    public String getVitaDockOauthKey() {
        return config.getVitaDockOauthKey();
    }

    public String getVitaDockOauthSecret() {
        return config.getVitaDockOauthSecret();
    }

    public String getSciStoreEndpoint() {
        return config.getSciStoreEndpoint();
    }

    public String getSciStoreUsername() {
        return config.getSciStoreUsername();
    }

    public String getSciStorePassword() {
        return config.getSciStorePassword();
    }

    public String getPacrEndpoint() {
        return config.getPacrEndpoint();
    }

    public String getPacrClientId() {
        return config.getPacrClientId();
    }

    public String getPacrClientUsername() {
        return config.getPacrClientUsername();
    }

    public String getPacrClientPassword() {
        return config.getPacrClientPassword();
    }

    public String getFakeS3Endpoint() {
        return config.getFakeS3Endpoint();
    }

    public boolean isDefinedFakeS3Endpoint() {
        return config.isDefinedFakeS3Endpoint();
    }

    public String getFakeS3EndpointPublishedURL() {
        return config.getFakeS3EndpointPublishedURL();
    }

    public boolean isClientCachingOfStaticFilesEnabled() {
        return config.isClientCachingOfStaticFilesEnabled();
    }

    public int getFileChunkSizeInBytes() {
        return config.getFileChunkSizeInBytes();
    }

    public int getDocDeleteBatchSize() {
        return config.getDocDeleteBatchSize();
    }

    public int getDocDeleteIntervalInHours() {
        return config.getDocDeleteIntervalInHours();
    }

    public Boolean getPrometheusReportingEnabled() {
        return config.getPrometheusReportingEnabled();
    }

    public String getLibraryUploadDirectory() {
        return config.getLibraryUploadDirectory();
    }

    public String getNonCryptoAccessKey() {
        return config.getNonCryptoAccessKey();
    }

    public String getNonCryptoSecretKey() {
        return config.getNonCryptoSecretKey();
    }

    public String getNonCryptoBucket() {
        return config.getNonCryptoBucket();
    }

    public String getProxyHost() {
        return config.getProxyHost();
    }

    public int getProxyPort() {
        return config.getProxyPort();
    }

    public String getEmisEsDownloadBaseDir() {
        return config.getEmisEsDownloadBaseDir();
    }

    public String getEmisEsSftpHost() {
        return config.getEmisEsSftpHost();
    }

    public int getEmisEsSftpPort() {
        return config.getEmisEsSftpPort();
    }

    public String getEmisEsSftpUser() {
        return config.getEmisEsSftpUser();
    }

    public String getEmisEsSftpPrivateKeyFile() {
        return config.getEmisEsSftpPrivateKeyFile();
    }

    public String getEmisEsSftpPrivateKeyFilePassphrase() {
        return config.getEmisEsSftpPrivateKeyFilePassphrase();
    }

    public String getEmisEsSftpRemoteDirectory() {
        return config.getEmisEsSftpRemoteDirectory();
    }

    public String getEmisEsPgpPrivateKeyFile() {
        return config.getEmisEsPgpPrivateKeyFile();
    }

    public String getEmisEsPgpPrivateKeyFilePassphrase() {
        return config.getEmisEsPgpPrivateKeyFilePassphrase();
    }

    public Boolean isEmisEsNotificationEnabled() {
        return config.isEmisEsNotificationEnabled();
    }

    public Set<String> getEmisEsPatientIdentifierFilterSet() {
        return config.getEmisEsPatientIdentifierFilterSet();
    }

    public String getEmisEsCsvEncoding() {
        return config.getEmisEsCsvEncoding();
    }

    public String getEmisEsConfigBaseDir() {
        return config.getEmisEsConfigBaseDir();
    }

    public Boolean isEmisEsWhiteListEnabled() {
        return config.isEmisEsWhiteListEnabled();
    }

    public int getEmisFailureGracePeriodHours() {
        return config.getEmisFailureGracePeriodHours();
    }

    public boolean getUseProxy() {
        return config.getUseProxy();
    }

    public boolean getProxyConfigured() {
        return config.getProxyConfigured();
    }

    public boolean getUseHttpForS3() {
        return config.getUseHttpForS3();
    }

    public String getSmtpHostname() {
        return config.getSmtpHostname();
    }

    public int getSmtpPort() {
        return config.getSmtpPort();
    }

    public String getSmtpUser() {
        return config.getSmtpUser();
    }

    public String getSmtpPassword() {
        return config.getSmtpPassword();
    }

    public String getSmtpAuth() {
        return config.getSmtpAuth();
    }

    public String getSmtpStartTlsEnable() {
        return config.getSmtpStartTlsEnable();
    }

    public String getSmtpSocketFactoryClass() {
        return config.getSmtpSocketFactoryClass();
    }

    public String getSmtpSocketFactoryFallback() {
        return config.getSmtpSocketFactoryFallback();
    }

    public String getTestTeamCoord() {
        return config.getTestTeamCoord();
    }

    public String getTestClinician() {
        return config.getTestClinician();
    }

    public String getTestPatient() {
        return config.getTestPatient();
    }

    public int getOrgAdminDashboardPageSize() {
        return config.getOrgAdminDashboardPageSize();
    }

    public String getCoreDevicesOrganizationId() {
        return config.getCoreDevicesOrganizationId();
    }

    public String getCoreDevicesAccessToken() {
        return config.getCoreDevicesAccessToken();
    }

    public int getSciStoreNewPatientLimit() {
        return config.getSciStoreNewPatientLimit();
    }

    public int getLatestResultsInitialThreshold() {
        return config.getLatestResultsInitialThreshold();
    }

    public int getHighchartsThreshold() {
        return config.getHighchartsThreshold();
    }

    public int getLetterInvitationTokenSize() {
        return config.getLetterInvitationTokenSize();
    }

    public int getLetterInvitationAccessCodeSize() {
        return config.getLetterInvitationAccessCodeSize();
    }

    public int getLetterInvitationExpiry() {
        return config.getLetterInvitationExpiry();
    }

    public long getUploadMaxFileSize() {
        return config.getUploadMaxFileSize();
    }

    public String getUploadMaxFileText() {
        return config.getUploadMaxFileText();
    }

    public long getImageUploadMaxFileSize() {
        return config.getImageUploadMaxFileSize();
    }

    public String getEmisEsJobCron() {
        return config.getEmisEsJobCron();
    }

    public String getSciStoreNotificationCron() {
        return config.getSciStoreNotificationCron();
    }

    public String getScriStorePatientSyncCron() {
        return config.getScriStorePatientSyncCron();
    }

    public String getChildBirthNotificationCron() {
        return config.getChildBirthNotificationCron();
    }

    public String getExpiredConsentRemoverCron() {
        return config.getExpiredConsentRemoverCron();
    }

    public String getSymptomsNotificationCron() {
        return config.getSymptomsNotificationCron();
    }

    public String getImageUploadMaxFileText() {
        return config.getImageUploadMaxFileText();
    }

    public long getGeneticsUploadMaxFileSize() {
        return config.getGeneticsUploadMaxFileSize();
    }

    public String getGeneticsUploadMaxFileText() {
        return config.getGeneticsUploadMaxFileText();
    }

    public boolean getSortEmisCsvs() {
        return config.getSortEmisCsvs();
    }

    public long getPlanUploadMaxFileSize() {
        return config.getPlanUploadMaxFileSize();
    }

    public String getPlanUploadMaxFileText() {
        return config.getPlanUploadMaxFileText();
    }

    public String getAuthorizationEndpointAddress() {
        return config.getAuthorizationEndpointAddress();
    }

    public String getRestApiClientId() {
        return config.getRestApiClientId();
    }

    public boolean isEmisEsEnabled() {
        return config.isEmisEsEnabled();
    }

    public boolean isTimelineEnabled() {
        return config.isTimelineEnabled();
    }

    public String getTimelineFrontendFetchURL() {
        return config.getTimelineFrontendFetchURL();
    }

    public String getTimelineFrontendBrowserBaseURL() {
        return config.getTimelineFrontendBrowserBaseURL();
    }

    public int getTimelineHtmlExpiryInSeconds() {
        return config.getTimelineHtmlExpiryInSeconds();
    }

    public String getLabtestsonlineSearchURL() {
        return config.getLabtestsonlineSearchURL();
    }

    public String getLabtestsonlineRefURL() {
        return config.getLabtestsonlineRefURL();
    }

    public String getHelpTextURL() {
        return config.getHelpTextURL();
    }

    public boolean isTrackingEnabled() {
        return config.isTrackingEnabled();
    }

    public String getEMISSSOMonitoringProfEmail() {
        return config.getEMISSSOMonitoringProfEmail();
    }

    public String getValidicURL() {
        return config.getValidicURL();
    }

    public int getValidicConnectionTimeoutMillis() {
        return config.getValidicConnectionTimeoutMillis();
    }

    public int getValidicReadTimeoutMillis() {
        return config.getValidicReadTimeoutMillis();
    }

    public long getComposeMessageMaxFileSize() {
        return config.getComposeMessageMaxFileSize();
    }

    public String getComposeMessageFileSizeText() {
        return config.getComposeMessageFileSizeText();
    }

    public boolean isCSRFProtectionEnabled() {
        return config.isCSRFProtectionEnabled();
    }

    public boolean isScistoreEnabled() {
        return config.isScistoreEnabled();
    }

    public int getMenudataQueryBatchSize() {
        return config.getMenudataQueryBatchSize();
    }

    public boolean isDisplayOfSensitiveErrorInformationEnabled() {
        return config.isDisplayOfSensitiveErrorInformationEnabled();
    }

    public boolean isSkipHL7IpWhitelistEnabled() {
        return config.isSkipHL7IpWhitelistEnabled();
    }

    public boolean isTestUsersEnabled() {
        return config.isTestUsersEnabled();
    }

    public boolean isFhirApiExperimental() {
        return config.isFhirApiExperimental();
    }

    public int getEmisEsProcessingBatchSize() {
        return config.getEmisEsProcessingBatchSize();
    }
}

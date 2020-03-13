package com.pkb.common.config;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This config class allows dynamic injection of values at runtime
 * It's designed to be used when the code is in test, for example to flip feature switches at runtime
 * For production code, use {@link ImmutableConfig}
 */
public class MutableConfig implements ConfigV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ConfigV2 defaultConfig;

    private Map<String, String> configMap;

    public MutableConfig(ConfigV2 defaultConfig) {
        this.defaultConfig = defaultConfig;
        this.configMap = new HashMap<>();
    }

    @Override
    public void setValue(String key, String value) {
        configMap.put(key, value);
    }

    @Override
    public void reset() {
        configMap.clear();
    }

    private Integer getIntValue(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private Long getLongValue(String longStr) {
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private Boolean getBooleanValue(String boolStr) {
        if (boolStr == null || (!boolStr.toLowerCase().equals("true") && !boolStr.toLowerCase().equals("false"))) {
            return null;
        }
        return Boolean.parseBoolean(boolStr);
    }

    private Path getPathValue(String pathStr) {
        try {
            if (pathStr != null) {
                return Paths.get(pathStr);
            }
        } catch (InvalidPathException ignored) {}
        return null;
    }

    @Override
    public String getBaseURL() {
        String baseUrl = configMap.get("baseUrl");
        if (baseUrl != null) {
            return baseUrl;
        }
        return defaultConfig.getBaseURL();
    }

    @Override
    public String getKMSBaseURL() {
        String kmsBaseUrl = configMap.get("kmsBaseUrl");
        if (kmsBaseUrl != null) {
            return kmsBaseUrl;
        }
        return defaultConfig.getKMSBaseURL();
    }

    @Override
    public int getMenudataQueryBatchSize() {
        Integer menuDataBatchSize = getIntValue(configMap.get("menuDataQueryBatchSize"));
        if (menuDataBatchSize != null) {
            return menuDataBatchSize;
        }
        return defaultConfig.getMenudataQueryBatchSize();
    }

    @Override
    public boolean isFakeDateTimeServiceEnabled() {
        Boolean fakeDateTimeServiceEnabled = getBooleanValue(configMap.get("fakeDataTimeServiceEnabled"));
        if (fakeDateTimeServiceEnabled != null) {
            return fakeDateTimeServiceEnabled;
        }
        return defaultConfig.isFakeDateTimeServiceEnabled();
    }

    @Override
    public String getVcMaxWarningDate() {
        String vcMaxWarningDate = configMap.get("vcMaxWarningRate");
        if (vcMaxWarningDate != null) {
            return vcMaxWarningDate;
        }
        return defaultConfig.getVcMaxWarningDate();
    }

    @Override
    public int getMaxBulkInvitationOutcomeReport() {
        Integer maxBulkInvitationOutcomeReport = getIntValue(configMap.get("maxBulkInvitationOutcomeReport"));
        if (maxBulkInvitationOutcomeReport != null) {
            return maxBulkInvitationOutcomeReport;
        }
        return defaultConfig.getMaxBulkInvitationOutcomeReport();
    }

    @Override
    public String uploadedDataSyncUrl() {
        String uploadedDataSyncUrl = configMap.get("uploadedDataSyncUrl");
        if (uploadedDataSyncUrl != null) {
            return uploadedDataSyncUrl;
        }
        return defaultConfig.uploadedDataSyncUrl();
    }

    @Override
    public long uploadedDataSyncConnectionTimeoutMilliseconds() {
        Long uploadedDataSyncConnectionTimeoutMilliseconds =
                getLongValue(configMap.get("uploadedDataSyncConnectionTimeout"));
        if (uploadedDataSyncConnectionTimeoutMilliseconds != null) {
            return uploadedDataSyncConnectionTimeoutMilliseconds;
        }
        return defaultConfig.uploadedDataSyncConnectionTimeoutMilliseconds();
    }

    @Override
    public long uploadedDataSyncWriteTimeoutMilliseconds() {
        Long uploadedDataSyncWriteTimeoutMilliseconds =
                getLongValue(configMap.get("uploadedDataSyncWriteTimeout"));
        if (uploadedDataSyncWriteTimeoutMilliseconds != null) {
            return uploadedDataSyncWriteTimeoutMilliseconds;
        }
        return defaultConfig.uploadedDataSyncWriteTimeoutMilliseconds();
    }

    @Override
    public long uploadedDataSyncReadTimeoutMilliseconds() {
        Long uploadedDataSyncReadTimeoutMilliseconds =
                getLongValue(configMap.get("uploadedDataSyncReadTimeout"));
        if (uploadedDataSyncReadTimeoutMilliseconds != null) {
            return uploadedDataSyncReadTimeoutMilliseconds;
        }
        return defaultConfig.uploadedDataSyncReadTimeoutMilliseconds();
    }

    @Override
    public boolean isExceptionForKmsFailureEnabled() {
        Boolean isExceptionForKmsFailureEnabled =
                getBooleanValue(configMap.get("exceptionForKmsFailure"));
        if (isExceptionForKmsFailureEnabled != null) {
            return isExceptionForKmsFailureEnabled;
        }
        return defaultConfig.isExceptionForKmsFailureEnabled();
    }

    @Override
    public boolean isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled() {
        Boolean isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled =
                getBooleanValue(configMap.get("exceptionForMultipleAccountForSingleNationalId"));
        if (isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled != null) {
            return isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled;
        }
        return defaultConfig.isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled();
    }

    @Override
    public int getInstituteUserFetchSize() {
        Integer instituteUserFetchSize =
                getIntValue(configMap.get("instituteUserFetchSize"));
        if (instituteUserFetchSize != null) {
            return instituteUserFetchSize;
        }
        return defaultConfig.getInstituteUserFetchSize();
    }

    @Override
    public int getAccountUserFetchSize() {
        Integer accountUserFetchSize = getIntValue(configMap.get("accountUserFetchSize"));
        if (accountUserFetchSize != null) {
            return accountUserFetchSize;
        }
        return defaultConfig.getAccountUserFetchSize();
    }

    @Override
    public int getUploadedDataFetchSize() {
        Integer uploadedDataFetchSize = getIntValue(configMap.get("uploadedDataFetchSize"));
        if (uploadedDataFetchSize != null) {
            return uploadedDataFetchSize;
        }
        return defaultConfig.getUploadedDataFetchSize();
    }

    @Override
    public boolean isFhirApiExperimental() {
        Boolean fhirApiExperimental = getBooleanValue(configMap.get("fhirApiExperimental"));
        if (fhirApiExperimental != null) {
            return fhirApiExperimental;
        }
        return defaultConfig.isFhirApiExperimental();
    }

    @Override
    public int getFhirObservationMaxNumberOfResources() {
        Integer fhirObsMaxResources = getIntValue(configMap.get("fhirObsMaxResources"));
        if (fhirObsMaxResources != null) {
            return fhirObsMaxResources;
        }
        return defaultConfig.getFhirObservationMaxNumberOfResources();
    }

    @Override
    public int getFhirAppointmentMaxNumberOfResources() {
        Integer fhirApptMaxResources = getIntValue(configMap.get("fhirApptMaxResources"));
        if (fhirApptMaxResources != null) {
            return fhirApptMaxResources;
        }
        return defaultConfig.getFhirAppointmentMaxNumberOfResources();
    }

    @Override
    public int getFhirCommunicationMaxNumberOfResources() {
        Integer fhirCommsMaxResources = getIntValue(configMap.get("fhirCommsMaxResources"));
        if (fhirCommsMaxResources != null) {
            return fhirCommsMaxResources;
        }
        return defaultConfig.getFhirCommunicationMaxNumberOfResources();
    }

    @Override
    public int getFhirDiagnosticReportMaxNumberOfResources() {
        Integer fhirDiagMaxResources = getIntValue(configMap.get("fhirDiagMaxResources"));
        if (fhirDiagMaxResources != null) {
            return fhirDiagMaxResources;
        }
        return defaultConfig.getFhirDiagnosticReportMaxNumberOfResources();
    }

    @Override
    public int getFhirEncounterMaxNumberOfResources() {
        Integer fhirEncounterMaxResources = getIntValue(configMap.get("fhirEncounterMaxResources"));
        if (fhirEncounterMaxResources != null) {
            return fhirEncounterMaxResources;
        }
        return defaultConfig.getFhirEncounterMaxNumberOfResources();
    }

    @Override
    public int getFhirDocumentReferenceMaxNumberOfResources() {
        Integer fhirDocRefMaxResources = getIntValue(configMap.get("fhirDocRefMaxResources"));
        if (fhirDocRefMaxResources != null) {
            return fhirDocRefMaxResources;
        }
        return defaultConfig.getFhirDocumentReferenceMaxNumberOfResources();
    }

    @Override
    public boolean isFhirCommunicationResourceEnabled() {
        Boolean fhirCommsResourceEnabled = getBooleanValue(configMap.get("fhirCommsResourceEnabled"));
        if (fhirCommsResourceEnabled != null) {
            return fhirCommsResourceEnabled;
        }
        return defaultConfig.isFhirCommunicationResourceEnabled();
    }

    @Override
    public String getSynertecApiClientId() {
        String synertecApiClientId = configMap.get("synertecApiClientId");
        if (synertecApiClientId != null) {
            return synertecApiClientId;
        }
        return defaultConfig.getSynertecApiClientId();
    }

    @Override
    public int getSlowDocRefQueryAlertThresholdSeconds() {
        Integer slowDocRefQueryAlertThreshold = getIntValue(configMap.get("slowDocRefQueryAlertThreshold"));
        if (slowDocRefQueryAlertThreshold != null) {
            return slowDocRefQueryAlertThreshold;
        }
        return defaultConfig.getSlowDocRefQueryAlertThresholdSeconds();
    }

    @Override
    public boolean isFhirAppointmentResourceEnabled() {
        Boolean fhirApptResourceEnabled = getBooleanValue(configMap.get("fhirApptResourceEnabled"));
        if (fhirApptResourceEnabled != null) {
            return fhirApptResourceEnabled;
        }
        return defaultConfig.isFhirAppointmentResourceEnabled();
    }

    @Override
    public boolean isFhirEncounterResourceEnabled() {
        Boolean fhirEncounterResourceEnabled = getBooleanValue(configMap.get("fhirEncounterResourceEnabled"));
        if (fhirEncounterResourceEnabled != null) {
            return fhirEncounterResourceEnabled;
        }
        return defaultConfig.isFhirEncounterResourceEnabled();
    }

    @Override
    public boolean isFhirPersonResourceEnabled() {
        Boolean fhirPersonResourceEnabled = getBooleanValue(configMap.get("fhirPersonResourceEnabled"));
        if (fhirPersonResourceEnabled != null) {
            return fhirPersonResourceEnabled;
        }
        return defaultConfig.isFhirPersonResourceEnabled();
    }

    @Override
    public boolean isFhirPatientResourceEnabled() {
        Boolean fhirPatientResourceEnabled = getBooleanValue(configMap.get("fhirPatientResourceEnabled"));
        if (fhirPatientResourceEnabled != null) {
            return fhirPatientResourceEnabled;
        }
        return defaultConfig.isFhirPatientResourceEnabled();
    }

    @Override
    public boolean isFhirPractitionerResourceEnabled() {
        Boolean fhirPractitionerResourceEnabled = getBooleanValue(configMap.get("fhirPractitionerResourceEnabled"));
        if (fhirPractitionerResourceEnabled != null) {
            return fhirPractitionerResourceEnabled;
        }
        return defaultConfig.isFhirPractitionerResourceEnabled();
    }

    @Override
    public boolean isFhirOrganizationResourceEnabled() {
        Boolean fhirOrganizationResourceEnabled = getBooleanValue(configMap.get("fhirOrganizationResourceEnabled"));
        if (fhirOrganizationResourceEnabled != null) {
            return fhirOrganizationResourceEnabled;
        }
        return defaultConfig.isFhirOrganizationResourceEnabled();
    }

    @Override
    public boolean isFhirNamingSystemResourceEnabled() {
        Boolean fhirNamingSystemResourceEnabled = getBooleanValue(configMap.get("fhirNamingSystemResourceEnabled"));
        if (fhirNamingSystemResourceEnabled != null) {
            return fhirNamingSystemResourceEnabled;
        }
        return defaultConfig.isFhirNamingSystemResourceEnabled();
    }

    @Override
    public boolean isFhirConsentResourceEnabled() {
        Boolean fhirConsentResourceEnabled = getBooleanValue(configMap.get("fhirConsentResourceEnabled"));
        if (fhirConsentResourceEnabled != null) {
            return fhirConsentResourceEnabled;
        }
        return defaultConfig.isFhirConsentResourceEnabled();
    }

    @Override
    public boolean isFhirPurviewOperationEnabled() {
        Boolean fhirPurviewOperationEnabled = getBooleanValue(configMap.get("fhirPurviewOperationEnabled"));
        if (fhirPurviewOperationEnabled != null) {
            return fhirPurviewOperationEnabled;
        }
        return defaultConfig.isFhirPurviewOperationEnabled();
    }

    @Override
    public boolean isFhirDiagnosticReportResourceEnabled() {
        Boolean fhirDiagReportEnabled = getBooleanValue(configMap.get("fhirDiagReportEnabled"));
        if (fhirDiagReportEnabled != null) {
            return fhirDiagReportEnabled;
        }
        return defaultConfig.isFhirDiagnosticReportResourceEnabled();
    }

    @Override
    public boolean isFhirDocumentReferenceResourceEnabled() {
        Boolean fhirDocRefEnabled = getBooleanValue(configMap.get("fhirDocRefEnabled"));
        if (fhirDocRefEnabled != null) {
            return fhirDocRefEnabled;
        }
        return defaultConfig.isFhirDocumentReferenceResourceEnabled();
    }

    @Override
    public boolean isFhirObservationResourceEnabled() {
        Boolean fhirObsEnabled = getBooleanValue(configMap.get("fhirObsEnabled"));
        if (fhirObsEnabled != null) {
            return fhirObsEnabled;
        }
        return defaultConfig.isFhirObservationResourceEnabled();
    }

    @Override
    public boolean isExceptionForNullUniqueIdEnabled() {
        Boolean exceptionForNullUniqueId = getBooleanValue(configMap.get("exceptionForNullUniqueId"));
        if (exceptionForNullUniqueId != null) {
            return exceptionForNullUniqueId;
        }
        return defaultConfig.isExceptionForNullUniqueIdEnabled();
    }

    @Override
    public boolean isExceptionForNullRouteEnabled() {
        Boolean exceptionForNullRoute = getBooleanValue(configMap.get("exceptionForNullRoute"));
        if (exceptionForNullRoute != null) {
            return exceptionForNullRoute;
        }
        return defaultConfig.isExceptionForNullRouteEnabled();
    }

    @Override
    public boolean isExceptionForNotCapturedRouteEnabled() {
        Boolean exceptionForNotCapturedRoute = getBooleanValue(configMap.get("exceptionForNotCapturedRoute"));
        if (exceptionForNotCapturedRoute != null) {
            return exceptionForNotCapturedRoute;
        }
        return defaultConfig.isExceptionForNotCapturedRouteEnabled();
    }

    @Override
    public boolean isExceptionForMissingSourceEnabled() {
        Boolean exceptionForMissingSource = getBooleanValue(configMap.get("exceptionForMissingSource"));
        if (exceptionForMissingSource != null) {
            return exceptionForMissingSource;
        }
        return defaultConfig.isExceptionForMissingSourceEnabled();
    }

    @Override
    public boolean isExceptionForNoUniqueIdInMessageEnabled() {
        Boolean exceptionForNoUniqueIdInMessage = getBooleanValue(configMap.get("exceptionForNoUniqueIdInMessage"));
        if (exceptionForNoUniqueIdInMessage != null) {
            return exceptionForNoUniqueIdInMessage;
        }
        return defaultConfig.isExceptionForNoUniqueIdInMessageEnabled();
    }

    @Override
    public boolean isExceptionForMissingPatientParticipantEnabled() {
        Boolean exceptionForMissingPatientParticipant = getBooleanValue(configMap.get("exceptionForMissingPatientParticipant"));
        if (exceptionForMissingPatientParticipant != null) {
            return exceptionForMissingPatientParticipant;
        }
        return defaultConfig.isExceptionForMissingPatientParticipantEnabled();
    }

    @Override
    public boolean isExceptionForNullRequestContextEnabled() {
        Boolean exceptionForNullRequestContext = getBooleanValue(configMap.get("exceptionForNullRequestContext"));
        if (exceptionForNullRequestContext != null) {
            return exceptionForNullRequestContext;
        }
        return defaultConfig.isExceptionForNullRequestContextEnabled();
    }

    @Override
    public boolean isExceptionForNullDefaultAccountIdEnabled() {
        Boolean exceptionForNullDefaultAccountId = getBooleanValue(configMap.get("exceptionForNullDefaultAccountId"));
        if (exceptionForNullDefaultAccountId != null) {
            return exceptionForNullDefaultAccountId;
        }
        return defaultConfig.isExceptionForNullDefaultAccountIdEnabled();
    }

    @Override
    public boolean isGpAppointmentBookingEnabled() {
        Boolean gpApptBookingEnabled = getBooleanValue(configMap.get("gpApptBookingEnabled"));
        if (gpApptBookingEnabled != null) {
            return gpApptBookingEnabled;
        }
        return defaultConfig.isGpAppointmentBookingEnabled();
    }

    @Override
    public boolean isGpPrescriptionOrderingEnabled() {
        Boolean gpPrescriptionOrderingEnabled = getBooleanValue(configMap.get("gpPrescriptionOrderingEnabled"));
        if (gpPrescriptionOrderingEnabled != null) {
            return gpPrescriptionOrderingEnabled;
        }
        return defaultConfig.isGpPrescriptionOrderingEnabled();
    }

    @Override
    public boolean isKmsKeyLoadingEnabled() {
        Boolean kmsKeyLoadingEnabled = getBooleanValue(configMap.get("kmsKeyLoadingEnabled"));
        if (kmsKeyLoadingEnabled != null) {
            return kmsKeyLoadingEnabled;
        }
        return defaultConfig.isKmsKeyLoadingEnabled();
    }

    @Override
    public boolean isNhsLoginEnabled() {
        Boolean nhsLoginEnabled = getBooleanValue(configMap.get("nhsLoginEnabled"));
        if (nhsLoginEnabled != null) {
            return nhsLoginEnabled;
        }
        return defaultConfig.isNhsLoginEnabled();
    }

    @Override
    public boolean isBookAppointmentWarningVisible() {
        Boolean bookApptWarningVisible = getBooleanValue(configMap.get("bookApptWarningVisible"));
        if (bookApptWarningVisible != null) {
            return bookApptWarningVisible;
        }
        return defaultConfig.isBookAppointmentWarningVisible();
    }

    @Override
    public String getFromEmailAddress() {
        String fromEmailAddress =  configMap.get("fromEmailAddress");
        if (fromEmailAddress != null) {
            return fromEmailAddress;
        }
        return defaultConfig.getFromEmailAddress();
    }

    @Override
    public String getFromEmailName() {
        String fromEmailName =  configMap.get("fromEmailName");
        if (fromEmailName != null) {
            return fromEmailName;
        }
        return defaultConfig.getFromEmailName();
    }

    @Override
    public int getInboxPageSize() {
        Integer inboxPageSize = getIntValue(configMap.get("inboxPageSize"));
        if (inboxPageSize != null) {
            return inboxPageSize;
        }
        return defaultConfig.getInboxPageSize();
    }

    @Override
    public int getPatientFilesPerPage() {
        Integer patientFilesPerPage = getIntValue(configMap.get("patientFilesPerPage"));
        if (patientFilesPerPage != null) {
            return patientFilesPerPage;
        }
        return defaultConfig.getPatientFilesPerPage();
    }

    @Override
    public int getPatientDiaryEntriesPerPage() {
        Integer patientDiaryEntriesPerPage = getIntValue(configMap.get("patientDiaryEntriesPerPage"));
        if (patientDiaryEntriesPerPage != null) {
            return patientDiaryEntriesPerPage;
        }
        return defaultConfig.getPatientDiaryEntriesPerPage();
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
        Path imageUploadLogoBaseDir = getPathValue(configMap.get("imageUploadLogoBaseDir"));
        if (imageUploadLogoBaseDir != null) {
            return imageUploadLogoBaseDir;
        }
        return defaultConfig.getImageUploadLogoBaseDirectory();
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
        Integer recipientColumnSize = getIntValue(configMap.get("recipientColumnSize"));
        if (recipientColumnSize != null) {
            return recipientColumnSize;
        }
        return defaultConfig.getRecipientColumnSize();
    }

    @Override
    public int getThumbnailImageWidth() {
        Integer thumbnailImageWidth = getIntValue(configMap.get("thumbnailImageWidth"));
        if (thumbnailImageWidth != null) {
            return thumbnailImageWidth;
        }
        return defaultConfig.getThumbnailImageWidth();
    }

    @Override
    public int getThumbnailImageHeight() {
        Integer thumbnailImageHeight = getIntValue(configMap.get("thumbnailImageHeight"));
        if (thumbnailImageHeight != null) {
            return thumbnailImageHeight;
        }
        return defaultConfig.getThumbnailImageHeight();
    }

    @Override
    public int getRadiologyReportExtractLength() {
        Integer radiologyReportExtractLength = getIntValue(configMap.get("radiologyReportExtractLength"));
        if (radiologyReportExtractLength != null) {
            return radiologyReportExtractLength;
        }
        return defaultConfig.getRadiologyReportExtractLength();
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
        Boolean sendReminderEmailsEnabled = getBooleanValue(configMap.get("sendReminderEmailsEnabled"));
        if (sendReminderEmailsEnabled != null) {
            return sendReminderEmailsEnabled;
        }
        return defaultConfig.getSendReminderEmailsEnabled();
    }

    @Override
    public boolean getUnreadMessageReminderEnabled() {
        Boolean sendUnreadMessageReminderEnabled = getBooleanValue(configMap.get("sendUnreadMessageReminderEnabled"));
        if (sendUnreadMessageReminderEnabled != null) {
            return sendUnreadMessageReminderEnabled;
        }
        return defaultConfig.getUnreadMessageReminderEnabled();
    }

    @Override
    public boolean getUnreadDocumentReminderEnabled() {
        Boolean sendUnreadDocumentReminderEnabled = getBooleanValue(configMap.get("getUnreadDocumentReminderEnabled"));
        if (sendUnreadDocumentReminderEnabled != null) {
            return sendUnreadDocumentReminderEnabled;
        }
        return defaultConfig.getUnreadDocumentReminderEnabled();
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
        Boolean definedFakeS3Endpoint = getBooleanValue(configMap.get("definedFakeS3Endpoint"));
        if (definedFakeS3Endpoint != null) {
            return definedFakeS3Endpoint;
        }
        return defaultConfig.isDefinedFakeS3Endpoint();
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
        Integer adminDashboardPageSize = getIntValue(configMap.get("adminDashboardPageSize"));
        if (adminDashboardPageSize != null) {
            return adminDashboardPageSize;
        }
        return defaultConfig.getOrgAdminDashboardPageSize();
    }

    @Override
    public int getAccessLogPageSize() {
        Integer accessLogPageSize = getIntValue(configMap.get("accessLogPageSize"));
        if (accessLogPageSize != null) {
            return accessLogPageSize;
        }
        return defaultConfig.getAccessLogPageSize();
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
        Integer sciStoreNewPatientLimit = getIntValue(configMap.get("sciStoreNewPatientLimit"));
        if (sciStoreNewPatientLimit != null) {
            return sciStoreNewPatientLimit;
        }
        return defaultConfig.getSciStoreNewPatientLimit();
    }

    @Override
    public int getLatestResultsInitialThreshold() {
        Integer latestResultsInitialThreshold = getIntValue(configMap.get("latestResultsInitialThreshold"));
        if (latestResultsInitialThreshold != null) {
            return latestResultsInitialThreshold;
        }
        return defaultConfig.getLatestResultsInitialThreshold();
    }

    @Override
    public int getHighchartsThreshold() {
        Integer highchartsThreshold = getIntValue(configMap.get("highchartsThreshold"));
        if (highchartsThreshold != null) {
            return highchartsThreshold;
        }
        return defaultConfig.getHighchartsThreshold();
    }

    @Override
    public boolean getUseUTCInHighcharts() {
        Boolean useUtcInHighcharts = getBooleanValue(configMap.get("useUtcInHighcharts"));
        if (useUtcInHighcharts != null) {
            return useUtcInHighcharts;
        }
        return defaultConfig.getUseUTCInHighcharts();
    }

    @Override
    public boolean getSortEmisCsvs() {
        Boolean sortEmisCsvs = getBooleanValue(configMap.get("sortEmisCsvs"));
        if (sortEmisCsvs != null) {
            return sortEmisCsvs;
        }
        return defaultConfig.getSortEmisCsvs();
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
        Integer fileChunkSizeInBytes = getIntValue(configMap.get("fileChunkSizeInBytes"));
        if (fileChunkSizeInBytes != null) {
            return fileChunkSizeInBytes;
        }
        return defaultConfig.getFileChunkSizeInBytes();
    }

    @Override
    public int getDocDeleteBatchSize() {
        Integer docDeleteBatchSize = getIntValue(configMap.get("docDeleteBatchSize"));
        if (docDeleteBatchSize != null) {
            return docDeleteBatchSize;
        }
        return defaultConfig.getDocDeleteBatchSize();
    }

    @Override
    public long getPatientBannerErrorTimeoutMillis() {
        Long patientBannerErrorTimeoutMillis =
                getLongValue(configMap.get("patientBannerErrorTimeoutMillis"));
        if (patientBannerErrorTimeoutMillis != null) {
            return patientBannerErrorTimeoutMillis;
        }
        return defaultConfig.getPatientBannerErrorTimeoutMillis();
    }

    @Override
    public int getDocDeleteIntervalInHours() {
        Integer docDeleteIntervalInHours = getIntValue(configMap.get("docDeleteIntervalInHours"));
        if (docDeleteIntervalInHours != null) {
            return docDeleteIntervalInHours;
        }
        return defaultConfig.getDocDeleteIntervalInHours();
    }

    @Override
    public Boolean getPrometheusReportingEnabled() {
        Boolean prometheusReportingEnabled = getBooleanValue(configMap.get("prometheusReportingEnabled"));
        if (prometheusReportingEnabled != null) {
            return prometheusReportingEnabled;
        }
        return defaultConfig.getPrometheusReportingEnabled();
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
        Integer proxyPort = getIntValue(configMap.get("proxyPort"));
        if (proxyPort != null) {
            return proxyPort;
        }
        return defaultConfig.getProxyPort();
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
        Boolean emisEsWhiteListEnabled = getBooleanValue(configMap.get("emisEsWhiteListEnabled"));
        if (emisEsWhiteListEnabled != null) {
            return emisEsWhiteListEnabled;
        }
        return defaultConfig.isEmisEsWhiteListEnabled();
    }

    @Override
    public int getEmisFailureGracePeriodHours() {
        Integer emisFailureGracePeriodHours = getIntValue(configMap.get("emisFailureGracePeriodHours"));
        if (emisFailureGracePeriodHours != null) {
            return emisFailureGracePeriodHours;
        }
        return defaultConfig.getEmisFailureGracePeriodHours();
    }

    @Override
    public Path getEmisEsSftpConfigYamlPath() {
        Path emisEsFtpConfigYamlPath = getPathValue(configMap.get("emisEsFtpConfigYamlPath"));
        if (emisEsFtpConfigYamlPath != null) {
            return emisEsFtpConfigYamlPath;
        }
        return defaultConfig.getEmisEsSftpConfigYamlPath();
    }

    @Override
    public Optional<Path> getEmisEsWhitelistYamlPath() {
        Path emisEsWhitelistYamlPath = getPathValue(configMap.get("emisEsWhitelistYamlPath"));
        if (emisEsWhitelistYamlPath != null) {
            return Optional.of(emisEsWhitelistYamlPath);
        }
        return defaultConfig.getEmisEsWhitelistYamlPath();
    }

    @Override
    public boolean getUseProxy() {
        Boolean useProxy = getBooleanValue(configMap.get("useProxy"));
        if (useProxy != null) {
            return useProxy;
        }
        return defaultConfig.getUseProxy();
    }

    @Override
    public boolean isProxyConfigured() {
        Boolean proxyConfigured = getBooleanValue(configMap.get("proxyConfigured"));
        if (proxyConfigured != null) {
            return proxyConfigured;
        }
        return defaultConfig.isProxyConfigured();
    }

    @Override
    public boolean getUseHttpForS3() {
        Boolean useHttpForS3 = getBooleanValue(configMap.get("useHttpForS3"));
        if (useHttpForS3 != null) {
            return useHttpForS3;
        }
        return defaultConfig.getUseHttpForS3();
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
        Integer smtpPort = getIntValue(configMap.get("smtpPort"));
        if (smtpPort != null) {
            return smtpPort;
        }
        return defaultConfig.getSmtpPort();
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
        Integer letterInvitationTokenSize = getIntValue(configMap.get("letterInvitationTokenSize"));
        if (letterInvitationTokenSize != null) {
            return letterInvitationTokenSize;
        }
        return defaultConfig.getLetterInvitationTokenSize();
    }

    @Override
    public int getLetterInvitationAccessCodeSize() {
        Integer letterInvitationAccessCodeSize = getIntValue(configMap.get("letterInvitationAccessCodeSize"));
        if (letterInvitationAccessCodeSize != null) {
            return letterInvitationAccessCodeSize;
        }
        return defaultConfig.getLetterInvitationAccessCodeSize();
    }

    @Override
    public int getLetterInvitationExpiry() {
        Integer letterInvitationExpiry = getIntValue(configMap.get("letterInvitationExpiry"));
        if (letterInvitationExpiry != null) {
            return letterInvitationExpiry;
        }
        return defaultConfig.getLetterInvitationExpiry();
    }

    @Override
    public long getUploadMaxFileSize() {
        Long uploadMaxFileSize = getLongValue(configMap.get("uploadMaxFileSize"));
        if (uploadMaxFileSize != null) {
            return uploadMaxFileSize;
        }
        return defaultConfig.getUploadMaxFileSize();
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
        Long imageUploadMaxFileSize = getLongValue(configMap.get("imageUploadMaxFileSize"));
        if (imageUploadMaxFileSize != null) {
            return imageUploadMaxFileSize;
        }
        return defaultConfig.getImageUploadMaxFileSize();
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
        Long geneticsUploadMaxFileSize = getLongValue(configMap.get("geneticsUploadMaxFileSize"));
        if (geneticsUploadMaxFileSize != null) {
            return geneticsUploadMaxFileSize;
        }
        return defaultConfig.getGeneticsUploadMaxFileSize();
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
        Long planUploadMaxFileSize = getLongValue(configMap.get("planUploadMaxFileSize"));
        if (planUploadMaxFileSize != null) {
            return planUploadMaxFileSize;
        }
        return defaultConfig.getPlanUploadMaxFileSize();
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
        Boolean emisEsEnabled = getBooleanValue(configMap.get("emisEsEnabled"));
        if (emisEsEnabled != null) {
            return emisEsEnabled;
        }
        return defaultConfig.isEmisEsEnabled();
    }

    @Override
    public boolean getBulkDisableSharingEnabled() {
        Boolean bulkDisableSharingEnabled = getBooleanValue(configMap.get("bulkDisableSharingEnabled"));
        if (bulkDisableSharingEnabled != null) {
            return bulkDisableSharingEnabled;
        }
        return defaultConfig.getBulkDisableSharingEnabled();
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
        Boolean rollBarEnabled = getBooleanValue(configMap.get("rollBarEnabled"));
        if (rollBarEnabled != null) {
            return rollBarEnabled;
        }
        return defaultConfig.isRollBarEnabled();
    }

    @Override
    public @Nullable String getRollBarEnvironment() {
        String rollBarEnvironment = configMap.get("rollBarEnvironment");
        if (rollBarEnvironment != null) {
            return rollBarEnvironment;
        }
        return defaultConfig.getRollBarEnvironment();
    }

    @Override
    public @Nullable String getRollbarEndpoint() {
        String rollBarEndpoint = configMap.get("rollBarEndpoint");
        if (rollBarEndpoint != null) {
            return rollBarEndpoint;
        }
        return defaultConfig.getRollbarEndpoint();
    }

    @Override
    public boolean isTimelineEnabled() {
        Boolean timelineEnabled = getBooleanValue(configMap.get("timelineEnabled"));
        if (timelineEnabled != null) {
            return timelineEnabled;
        }
        return defaultConfig.isTimelineEnabled();
    }

    @Override
    public boolean isTimelineCalendarEnabled() {
        Boolean timelineCalendarEnabled = getBooleanValue(configMap.get("timelineCalendarEnabled"));
        if (timelineCalendarEnabled != null) {
            return timelineCalendarEnabled;
        }
        return defaultConfig.isTimelineCalendarEnabled();
    }

    @Override
    public boolean isTimelineTestsEnabled() {
        Boolean timelineTestsEnabled = getBooleanValue(configMap.get("timelineTestsEnabled"));
        if (timelineTestsEnabled != null) {
            return timelineTestsEnabled;
        }
        return defaultConfig.isTimelineTestsEnabled();
    }

    @Override
    public boolean isTimelineRadiologyEnabled() {
        Boolean timelineRadiologyEnabled = getBooleanValue(configMap.get("timelineRadiologyEnabled"));
        if (timelineRadiologyEnabled != null) {
            return timelineRadiologyEnabled;
        }
        return defaultConfig.isTimelineRadiologyEnabled();
    }

    @Override
    public boolean isTimelineSymptomsEnabled() {
        Boolean timelineSymptomsEnabled = getBooleanValue(configMap.get("timelineSymptomsEnabled"));
        if (timelineSymptomsEnabled != null) {
            return timelineSymptomsEnabled;
        }
        return defaultConfig.isTimelineSymptomsEnabled();
    }

    @Override
    public boolean isTimelineMeasurementsEnabled() {
        Boolean timelineMeasurementsEnabled = getBooleanValue(configMap.get("timelineMeasurementsEnabled"));
        if (timelineMeasurementsEnabled != null) {
            return timelineMeasurementsEnabled;
        }
        return defaultConfig.isTimelineMeasurementsEnabled();
    }

    @Override
    public boolean isTimelinePreloadingEnabled() {
        Boolean timelinePreloadingEnabled = getBooleanValue(configMap.get("timelinePreloadingEnabled"));
        if (timelinePreloadingEnabled != null) {
            return timelinePreloadingEnabled;
        }
        return defaultConfig.isTimelinePreloadingEnabled();
    }

    @Override
    public int getTimelineRequestTimeout() {
        Integer timelineRequestTimeout = getIntValue(configMap.get("timelineRequestTimeout"));
        if (timelineRequestTimeout != null) {
            return timelineRequestTimeout;
        }
        return defaultConfig.getTimelineRequestTimeout();
    }

    @Override
    public boolean isPatientBannerEnabled() {
        Boolean patientBannerEnabled = getBooleanValue(configMap.get("patientBannerEnabled"));
        if (patientBannerEnabled != null) {
            return patientBannerEnabled;
        }
        return defaultConfig.isPatientBannerEnabled();
    }

    @Override
    public boolean isRejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3() {
        Boolean rejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3
                = getBooleanValue(configMap.get("rejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3"));
        if (rejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3 != null) {
            return rejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3;
        }
        return defaultConfig.isRejectingNonTextHL7ORUR01MessagesWithOneOBRHavingMultipleOBX3();
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
        Integer timelineHtmlExpiryInSeconds = getIntValue(configMap.get("timelineHtmlExpiryInSeconds"));
        if (timelineHtmlExpiryInSeconds != null) {
            return timelineHtmlExpiryInSeconds;
        }
        return defaultConfig.getTimelineHtmlExpiryInSeconds();
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
        Integer validicConnectionTimeoutMillis = getIntValue(configMap.get("validicConnectionTimeoutMillis"));
        if (validicConnectionTimeoutMillis != null) {
            return validicConnectionTimeoutMillis;
        }
        return defaultConfig.getValidicConnectionTimeoutMillis();
    }

    @Override
    public int getValidicReadTimeoutMillis() {
        Integer validicReadTimeoutMillis = getIntValue(configMap.get("validicReadTimeoutMillis"));
        if (validicReadTimeoutMillis != null) {
            return validicReadTimeoutMillis;
        }
        return defaultConfig.getValidicReadTimeoutMillis();
    }

    @Override
    public long getComposeMessageMaxFileSize() {
        Long composeMessageMaxFileSize =
                getLongValue(configMap.get("composeMessageMaxFileSize"));
        if (composeMessageMaxFileSize != null) {
            return composeMessageMaxFileSize;
        }
        return defaultConfig.getComposeMessageMaxFileSize();
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
        Boolean csrfProtectionEnabled = getBooleanValue(configMap.get("csrfProtectionEnabled"));
        if (csrfProtectionEnabled != null) {
            return csrfProtectionEnabled;
        }
        return defaultConfig.isCSRFProtectionEnabled();
    }

    @Override
    public boolean isTrackingEnabled() {
        Boolean trackingEnabled = getBooleanValue(configMap.get("trackingEnabled"));
        if (trackingEnabled != null) {
            return trackingEnabled;
        }
        return defaultConfig.isTrackingEnabled();
    }

    @Override
    public int getMatomoSiteId() {
        Integer matomoSiteId = getIntValue(configMap.get("matomoSiteId"));
        if (matomoSiteId != null) {
            return matomoSiteId;
        }
        return defaultConfig.getMatomoSiteId();
    }

    @Override
    public boolean isScistoreEnabled() {
        Boolean sciStoreEnabled = getBooleanValue(configMap.get("sciStoreEnabled"));
        if (sciStoreEnabled != null) {
            return sciStoreEnabled;
        }
        return defaultConfig.isScistoreEnabled();
    }

    @Override
    public int getDBQueryBatchSize() {
        Integer queryBatchSize = getIntValue(configMap.get("queryBatchSize"));
        if (queryBatchSize != null) {
            return queryBatchSize;
        }
        return defaultConfig.getDBQueryBatchSize();
    }

    @Override
    public boolean isClientCachingOfStaticFilesEnabled() {
        Boolean clientCachingOfStaticFilesEnabled = getBooleanValue(configMap.get("clientCachingOfStaticFilesEnabled"));
        if (clientCachingOfStaticFilesEnabled != null) {
            return clientCachingOfStaticFilesEnabled;
        }
        return defaultConfig.isClientCachingOfStaticFilesEnabled();
    }

    @Override
    public boolean isDisplayOfSensitiveErrorInformationEnabled() {
        Boolean displayOfSensitiveErrorInformationEnabled = getBooleanValue(configMap.get("displayOfSensitiveErrorInformationEnabled"));
        if (displayOfSensitiveErrorInformationEnabled != null) {
            return displayOfSensitiveErrorInformationEnabled;
        }
        return defaultConfig.isDisplayOfSensitiveErrorInformationEnabled();
    }

    @Override
    public boolean isHL7IpWhitelistEnabled() {
        Boolean hl7IpWhitelistEnabled = getBooleanValue(configMap.get("hl7IpWhitelistEnabled"));
        if (hl7IpWhitelistEnabled != null) {
            return hl7IpWhitelistEnabled;
        }
        return defaultConfig.isHL7IpWhitelistEnabled();
    }

    @Override
    public boolean isTestUsersEnabled() {
        Boolean testUsersEnabled = getBooleanValue(configMap.get("testUsersEnabled"));
        if (testUsersEnabled != null) {
            return testUsersEnabled;
        }
        return defaultConfig.isTestUsersEnabled();
    }

    @Override
    public int getHl7QryA19PageSize() {
        Integer hl7QryA19PageSize = getIntValue(configMap.get("hl7QryA19PageSize"));
        if (hl7QryA19PageSize != null) {
            return hl7QryA19PageSize;
        }
        return defaultConfig.getHl7QryA19PageSize();
    }

    @Override
    public boolean isHL7MdmT11DocumentDeletionEnabled() {
        Boolean hl7MdmT11DocumentDeletionEnabled = getBooleanValue(configMap.get("hl7MdmT11DocumentDeletionEnabled"));
        if (hl7MdmT11DocumentDeletionEnabled != null) {
            return hl7MdmT11DocumentDeletionEnabled;
        }
        return defaultConfig.isHL7MdmT11DocumentDeletionEnabled();
    }

    @Override
    public boolean isHL7MdmT01DocumentUpdateEnabled() {
        Boolean hl7MdmT01DocumentUpdateEnabled = getBooleanValue(configMap.get("hl7MdmT01DocumentUpdateEnabled"));
        if (hl7MdmT01DocumentUpdateEnabled != null) {
            return hl7MdmT01DocumentUpdateEnabled;
        }
        return defaultConfig.isHL7MdmT01DocumentUpdateEnabled();
    }

    @Override
    public int getNumberOfRecentLoginAttemptsAllowed() {
        Integer numberOfRecentLoginAttemptsAllowed = getIntValue(configMap.get("numberOfRecentLoginAttemptsAllowed"));
        if (numberOfRecentLoginAttemptsAllowed != null) {
            return numberOfRecentLoginAttemptsAllowed;
        }
        return defaultConfig.getNumberOfRecentLoginAttemptsAllowed();
    }

    @Override
    public int getDefinitionOfRecentLoginAttemptInSeconds() {
        Integer definitionOfRecentLoginAttemptInSeconds = getIntValue(configMap.get("definitionOfRecentLoginAttemptInSeconds"));
        if (definitionOfRecentLoginAttemptInSeconds != null) {
            return definitionOfRecentLoginAttemptInSeconds;
        }
        return defaultConfig.getDefinitionOfRecentLoginAttemptInSeconds();
    }

    @Override
    public int getEmisEsProcessingBatchSize() {
        Integer emisEsProcessingBatchSize = getIntValue(configMap.get("emisEsProcessingBatchSize"));
        if (emisEsProcessingBatchSize != null) {
            return emisEsProcessingBatchSize;
        }
        return defaultConfig.getEmisEsProcessingBatchSize();
    }

    @Override
    public int getEmisEsCodingBatchSize() {
        Integer emisEsCodingBatchSize = getIntValue(configMap.get("emisEsCodingBatchSize"));
        if (emisEsCodingBatchSize != null) {
            return emisEsCodingBatchSize;
        }
        return defaultConfig.getEmisEsCodingBatchSize();
    }

    @Override
    public boolean isEmisEsStateResumeEnabled() {
        Boolean emisEsStateResumeEnabled = getBooleanValue(configMap.get("emisEsStateResumeEnabled"));
        if (emisEsStateResumeEnabled != null) {
            return emisEsStateResumeEnabled;
        }
        return defaultConfig.isEmisEsStateResumeEnabled();
    }

    @Override
    public int getAutosaveTimeoutInMilliseconds() {
        Integer autosaveTimeoutInMilliseonds = getIntValue(configMap.get("autosaveTimeoutInMilliseonds"));
        if (autosaveTimeoutInMilliseonds != null) {
            return autosaveTimeoutInMilliseonds;
        }
        return defaultConfig.getAutosaveTimeoutInMilliseconds();
    }

    @Override
    public int getAppointmentsCalendarMonths() {
        Integer appointmentsCalendarMonths = getIntValue(configMap.get("appointmentsCalendarMonths"));
        if (appointmentsCalendarMonths != null) {
            return appointmentsCalendarMonths;
        }
        return defaultConfig.getAppointmentsCalendarMonths();
    }

    @Override
    public Optional<String> clamAvHost() {
        String clamAvHost = configMap.get("clamAvHost");
        if (clamAvHost != null) {
            return Optional.of(clamAvHost);
        }
        return defaultConfig.clamAvHost();
    }

    @Override
    public boolean isFakeHelpPageBaseUrlProviderEnabled() {
        Boolean fakeHelpPageBaseUrlProviderEnabled = getBooleanValue(configMap.get("fakeHelpPageBaseUrlProviderEnabled"));
        if (fakeHelpPageBaseUrlProviderEnabled != null) {
            return fakeHelpPageBaseUrlProviderEnabled;
        }
        return defaultConfig.isFakeHelpPageBaseUrlProviderEnabled();
    }

    @Override
    public int getEncounterTimelineRangeMonths() {
        Integer encounterTimelineRangeMonths = getIntValue(configMap.get("encounterTimelineRangeMonths"));
        if (encounterTimelineRangeMonths != null) {
            return encounterTimelineRangeMonths;
        }
        return defaultConfig.getEncounterTimelineRangeMonths();
    }

    @Override
    public int getUserAgentAnalyzerCacheSize() {
        Integer userAgentAnalyzerCacheSize = getIntValue(configMap.get("userAgentAnalyzerCacheSize"));
        if (userAgentAnalyzerCacheSize != null) {
            return userAgentAnalyzerCacheSize;
        }
        return defaultConfig.getUserAgentAnalyzerCacheSize();
    }

    @Override
    public boolean enforceSecureCookies() {
        Boolean enforceSecureCookies = getBooleanValue(configMap.get("enforceSecureCookies"));
        if (enforceSecureCookies != null) {
            return enforceSecureCookies;
        }
        return defaultConfig.enforceSecureCookies();
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
        Integer orgNetworkSyncTransactionTimeout = getIntValue(configMap.get("orgNetworkSyncTransactionTimeout"));
        if (orgNetworkSyncTransactionTimeout != null) {
            return orgNetworkSyncTransactionTimeout;
        }
        return defaultConfig.orgNetworkSyncTransactionTimeout();
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
        Integer emisRestConnectTimeout = getIntValue(configMap.get("emisRestConnectTimeout"));
        if (emisRestConnectTimeout != null) {
            return emisRestConnectTimeout;
        }
        return defaultConfig.getEmisRestConnectTimeout();
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
        Integer odsCacheSize = getIntValue(configMap.get("odsCacheSize"));
        if (odsCacheSize != null) {
            return odsCacheSize;
        }
        return defaultConfig.getOdsCacheSize();
    }

    @Override
    public int getOdsCacheExpiryHours() {
        Integer odsCacheExpiryHours = getIntValue(configMap.get("odsCacheExpiryHours"));
        if (odsCacheExpiryHours != null) {
            return odsCacheExpiryHours;
        }
        return defaultConfig.getOdsCacheExpiryHours();
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
        Integer unregisteredPatientsPageSize = getIntValue(configMap.get("unregisteredPatientsPageSize"));
        if (unregisteredPatientsPageSize != null) {
            return unregisteredPatientsPageSize;
        }
        return defaultConfig.getUnregisteredPatientsPageSize();
    }

    @Override
    public int getUploadedDataBatchSyncSize() {
        Integer uploadedDataBatchSyncSize = getIntValue(configMap.get("uploadedDataBatchSyncSize"));
        if (uploadedDataBatchSyncSize != null) {
            return uploadedDataBatchSyncSize;
        }
        return defaultConfig.getUploadedDataBatchSyncSize();
    }

    @Override
    public boolean isNhsLoginWebUiButtonEnabled() {
        Boolean nhsLoginWebUiButtonEnabled = getBooleanValue(configMap.get("nhsLoginWebUiButtonEnabled"));
        if (nhsLoginWebUiButtonEnabled != null) {
            return nhsLoginWebUiButtonEnabled;
        }
        return defaultConfig.isNhsLoginWebUiButtonEnabled();
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
    public boolean isMutableConfigEnabled() {
        return defaultConfig.isMutableConfigEnabled();
    }
}

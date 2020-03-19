package com.pkb.common.config;

public interface PkbPluginConfig extends BasePhrConfig {

    String getKMSBaseURL();
    int getMenudataQueryBatchSize();
    String getVcMaxWarningDate();
    String uploadedDataSyncUrl();
    long uploadedDataSyncConnectionTimeoutMilliseconds();
    long uploadedDataSyncWriteTimeoutMilliseconds();
    long uploadedDataSyncReadTimeoutMilliseconds();
    boolean isExceptionForKmsFailureEnabled();
    boolean isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled();
    int getInstituteUserFetchSize();
    int getAccountUserFetchSize();
    int getUploadedDataFetchSize();
    boolean isExceptionForNullUniqueIdEnabled();
    boolean isExceptionForNullRouteEnabled();
    boolean isExceptionForNotCapturedRouteEnabled();
    boolean isExceptionForMissingSourceEnabled();
    boolean isExceptionForNoUniqueIdInMessageEnabled();
    boolean isExceptionForNullDefaultAccountIdEnabled();
    boolean isKmsKeyLoadingEnabled();
    String getSynertecApiClientId();
    int getSlowDocRefQueryAlertThresholdSeconds();
}

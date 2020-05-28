package com.pkb.common.config;

public interface PkbPluginConfig extends PrimaryBaseConfig {

    String getKMSBaseURL();
    int getMenudataQueryBatchSize();
    String getVcMaxWarningDate();
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

package com.pkb.common.config;

public class MutablePhrPluginConfig extends BaseMutableConfig implements PkbPluginConfig {

    private PkbPluginConfig defaultConfig;

    public MutablePhrPluginConfig(PkbPluginConfig defaultConfig) {
        super();
        this.defaultConfig = defaultConfig;
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
        return getIntValue(configMap.get("menuDataQueryBatchSize"))
                .orElseGet(() -> defaultConfig.getMenudataQueryBatchSize());
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
    public boolean isExceptionForKmsFailureEnabled() {
        return getBooleanValue(configMap.get("exceptionForKmsFailure"))
                .orElseGet(() -> defaultConfig.isExceptionForKmsFailureEnabled());
    }

    @Override
    public boolean isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled() {
        return getBooleanValue(configMap.get("exceptionForMultipleAccountForSingleNationalId"))
                .orElseGet(() -> defaultConfig.isExceptionForMultipleAccountForSingleNationalIdExceptionEnabled());
    }

    @Override
    public int getInstituteUserFetchSize() {
        return getIntValue(configMap.get("instituteUserFetchSize"))
                .orElseGet(() -> defaultConfig.getInstituteUserFetchSize());
    }

    @Override
    public int getAccountUserFetchSize() {
        return getIntValue(configMap.get("accountUserFetchSize"))
                .orElseGet(() -> defaultConfig.getAccountUserFetchSize());
    }

    @Override
    public int getUploadedDataFetchSize() {
        return getIntValue(configMap.get("uploadedDataFetchSize"))
                .orElseGet(() -> defaultConfig.getUploadedDataFetchSize());
    }

    @Override
    public boolean isExceptionForNullUniqueIdEnabled() {
        return getBooleanValue(configMap.get("exceptionForNullUniqueId"))
                .orElseGet(() -> defaultConfig.isExceptionForNullUniqueIdEnabled());
    }

    @Override
    public boolean isExceptionForNullRouteEnabled() {
        return getBooleanValue(configMap.get("exceptionForNullRoute"))
                .orElseGet(() -> defaultConfig.isExceptionForNullRouteEnabled());
    }

    @Override
    public boolean isExceptionForNotCapturedRouteEnabled() {
        return getBooleanValue(configMap.get("exceptionForNotCapturedRoute"))
                .orElseGet(() -> defaultConfig.isExceptionForNotCapturedRouteEnabled());
    }

    @Override
    public boolean isExceptionForMissingSourceEnabled() {
        return getBooleanValue(configMap.get("exceptionForMissingSource"))
                .orElseGet(() -> defaultConfig.isExceptionForMissingSourceEnabled());
    }

    @Override
    public boolean isExceptionForNoUniqueIdInMessageEnabled() {
        return getBooleanValue(configMap.get("exceptionForNoUniqueIdInMessage"))
                .orElseGet(() -> defaultConfig.isExceptionForNoUniqueIdInMessageEnabled());
    }

    @Override
    public boolean isExceptionForNullDefaultAccountIdEnabled() {
        return getBooleanValue(configMap.get("exceptionForNullDefaultAccountId"))
                .orElseGet(() -> defaultConfig.isExceptionForNullDefaultAccountIdEnabled());
    }

    @Override
    public boolean isKmsKeyLoadingEnabled() {
        return getBooleanValue(configMap.get("kmsKeyLoadingEnabled"))
                .orElseGet(() -> defaultConfig.isKmsKeyLoadingEnabled());
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
        return getIntValue(configMap.get("slowDocRefQueryAlertThreshold"))
                .orElseGet(() -> defaultConfig.getSlowDocRefQueryAlertThresholdSeconds());
    }

    @Override
    BaseConfig getDefaultConfig() {
        return defaultConfig;
    }
}

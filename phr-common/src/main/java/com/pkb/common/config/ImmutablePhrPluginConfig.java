package com.pkb.common.config;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmutablePhrPluginConfig extends BaseImmutableConfig implements PkbPluginConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ImmutablePhrPluginConfig(RawConfigStorage storage) {
        super(storage);
    }

    @Override
    public int getMenudataQueryBatchSize() {
        return storage.getInt("menudataQueryBatchSize", 5000);
    }

    @Override
    public String getVcMaxWarningDate() {
        return storage.getString("vc_max_warning_date", "2018-07-26T00:00:00Z");
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
    public boolean isExceptionForNullDefaultAccountIdEnabled() {
        return storage.getBoolean("consistency.check.pkbperson.defaultaccountid.null.exception", false);
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

    @Override
    public boolean isKmsKeyLoadingEnabled() {
        return storage.getBoolean("kms.key.loading.enabled", false);
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
    public String getSynertecApiClientId() {
        return storage.getString("synertecApiClientId", "synertec-user-test");
    }

    @Override
    public int getSlowDocRefQueryAlertThresholdSeconds() {
        return storage.getInt("slowDocRefQueryAlertThresholdSeconds", 30);
    }
}

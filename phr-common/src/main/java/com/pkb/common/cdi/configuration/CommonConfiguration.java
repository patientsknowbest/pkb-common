package com.pkb.common.cdi.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.pkb.common.config.FhirConfig;
import com.pkb.common.config.ImmutableFhirConfig;
import com.pkb.common.config.ImmutablePhrPluginConfig;
import com.pkb.common.config.MutableFhirConfig;
import com.pkb.common.config.MutablePhrPluginConfig;
import com.pkb.common.config.PkbPluginConfig;
import com.pkb.common.config.RawConfigStorage;
import com.pkb.common.datetime.DateTimeService;
import com.pkb.common.datetime.DefaultDateTimeService;
import com.pkb.common.datetime.FakeDateTimeService;
import com.pkb.common.testlogging.DefaultDetailLoggingProvider;
import com.pkb.common.testlogging.DetailLoggingProvider;
import com.pkb.service.uuid.UUIDProvider;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import okhttp3.OkHttpClient;

@ApplicationScoped
public class CommonConfiguration {

    @Produces
    @ApplicationScoped
    public PkbPluginConfig pluginConfig() {
        PkbPluginConfig immutableConfig = new ImmutablePhrPluginConfig(RawConfigStorage.createDefault());
        if (immutableConfig.isMutableConfigEnabled()) {
            return new MutablePhrPluginConfig(immutableConfig);
        } else {
            return immutableConfig;
        }
    }

    @Produces
    @ApplicationScoped
    public FhirConfig fhirConfig() {
        FhirConfig immutableConfig = new ImmutableFhirConfig(RawConfigStorage.createDefault());
        if (immutableConfig.isMutableConfigEnabled()) {
            return new MutableFhirConfig(immutableConfig);
        } else {
            return immutableConfig;
        }
    }

    @Produces
    @ApplicationScoped
    public DetailLoggingProvider defaultTestLoggingService() {
        return DefaultDetailLoggingProvider.INSTANCE;
    }

    @Produces
    @ApplicationScoped
    public DateTimeService dateTimeService(PkbPluginConfig config) {
        if (config.isFakeDateTimeServiceEnabled()) {
            return new FakeDateTimeService();
        } else {
            return new DefaultDateTimeService();
        }
    }

    @Produces
    @ApplicationScoped
    public UUIDProvider getUUIDProvider() {
        return new UUIDProvider();
    }

    @Produces
    @ApplicationScoped
    public OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .eventListener(OkHttpMetricsEventListener.builder(Metrics.globalRegistry, "okhttp.requests").build())
                .build();
    }
}

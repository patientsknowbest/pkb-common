package com.pkb.common.hapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.google.common.base.Preconditions;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpConnectionPoolMetrics;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.rest.api.EncodingEnum.JSON;

public class MonitoredGenericClientBuilder {

    private GenericClientConfiguration configuration;

    private MeterRegistry registry;

    private FhirContext fhirContext;

    private OkHttpClient client;

    private ConnectionPool connectionPool;

    private String url;

    public static MonitoredGenericClientBuilder newBuilder() {
        return new MonitoredGenericClientBuilder();
    }

    public MonitoredGenericClientBuilder setConfiguration(GenericClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public MonitoredGenericClientBuilder setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        return this;
    }

    public MonitoredGenericClientBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public MonitoredGenericClientBuilder setRegistry(MeterRegistry registry) {
        this.registry = registry;
        return this;
    }

    public MonitoredGenericClientBuilder setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public MonitoredGenericClientBuilder setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        return this;
    }


    public IGenericClient build() {

        Preconditions.checkNotNull(configuration, "configuration must be set");
        Preconditions.checkNotNull(fhirContext, "fhirContext must be set");
        Preconditions.checkNotNull(url, "url must be set");

        if (registry == null) {
            registry = Metrics.globalRegistry;
        }

        if (client == null) {
            client = new OkHttpClient.Builder()
                    .eventListener(OkHttpMetricsEventListener.builder(registry, "okhttp.requests").build())
                    .build();
        }

        if (connectionPool == null) {
            connectionPool = new ConnectionPool(configuration.getMaxIdleConnections(), 5, TimeUnit.MINUTES);
            new OkHttpConnectionPoolMetrics(connectionPool).bindTo(registry);
        }

        var newClient = client.newBuilder()
                .connectTimeout(configuration.getConnectTimeout())
                .readTimeout(configuration.getReadTimeout())
                .writeTimeout(configuration.getWriteTimeout())
                .retryOnConnectionFailure(configuration.getRetryOnConnectionFailure())
                .connectionPool(connectionPool)
                .build();

        MonitoredOkHttpRestfulClientFactory clientFactory = new MonitoredOkHttpRestfulClientFactory(fhirContext, newClient);
        fhirContext.setRestfulClientFactory(clientFactory);

        var client = fhirContext.newRestfulGenericClient(url);
        client.setEncoding(JSON);
        return client;
    }

}

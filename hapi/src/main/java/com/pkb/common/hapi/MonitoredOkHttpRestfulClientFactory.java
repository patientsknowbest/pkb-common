package com.pkb.common.hapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * This is basically a re-implementation of HAPI's OkHttpRestfulClientFactory with two specific improvements:
 * <ol>
 *     <li>
 *         It uses a client template rather than creating a new client from scratch, which means that any config set
 *         on the client (such as metrics integration) is not lost when [resetHttpClient] is called
 *     </li>
 *     <li>
 *         The target FHIR resource is identified when the client is accessed and correctly recorded in the `uri`
 *         tag on the outgoing metric
 *     </li>
 * </ol>
 *
 * See also:
 * <ul>
 *     <li>{@link ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory}</li>
 *     <li><a href="https://hapifhir.io/hapi-fhir/docs/client/client_configuration.html">HAPI client configuration docs</a></li>
 * </ul>
 */
public final class MonitoredOkHttpRestfulClientFactory extends RestfulClientFactory {

    private final OkHttpClient clientTemplate;

    private Call.Factory nativeClient;

    private Function<String, String> metricFn;

    public MonitoredOkHttpRestfulClientFactory(FhirContext fhirContext, OkHttpClient clientTemplate) {
        this(fhirContext, clientTemplate, null);
    }

    public MonitoredOkHttpRestfulClientFactory(
            @NotNull FhirContext fhirContext,
            @NotNull OkHttpClient clientTemplate,
            @Nullable Function<String, String> metricFn) {

        super(fhirContext);

        this.clientTemplate = clientTemplate;
        this.metricFn = metricFn;
    }

    @Override
    @NotNull
    public IHttpClient getHttpClient(@NotNull String theServerBase) {
        return new OkHttpRestfulClient(
                this.getNativeClient(),
                new StringBuilder(theServerBase),
                null,
                null,
                null,
                null);
    }

    @Override
    @NotNull
    public IHttpClient getHttpClient(
            StringBuilder url,
            Map<String, List<String>> params,
            String ifNoneExist,
            RequestTypeEnum requestType,
            List<Header> headers) {

        if (metricFn != null) {
            String resource = metricFn.apply(url.toString());
            if (resource != null && headers != null) {
                headers.add(new Header(OkHttpMetricsEventListener.URI_PATTERN, resource));
            }
        }

        return new OkHttpRestfulClient(
                this.getNativeClient(),
                url,
                params,
                ifNoneExist,
                requestType,
                headers);
    }

    @Override
    public void setHttpClient(Object httpClient) {
        this.nativeClient = (Call.Factory) httpClient;
    }

    @Override
    public void setProxy(String theHost, Integer thePort) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(theHost, thePort));
        OkHttpClient.Builder builder = ((OkHttpClient) getNativeClient()).newBuilder().proxy(proxy);
        setHttpClient(builder.build());
    }

    @Override
    protected void resetHttpClient() {
        this.nativeClient = null;
    }

    private synchronized Call.Factory getNativeClient() {
        if (nativeClient == null) {
            // IJ doesn't like this for some reason, even though it compiles ok and okhttp3.OkHttpClient does in fact
            // implement Call.Factory ¯\_(ツ)_/¯
            //noinspection CastToIncompatibleInterface
            nativeClient = (Call.Factory) clientTemplate.newBuilder()
                    .connectTimeout(getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(getSocketTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(getSocketTimeout(), TimeUnit.MILLISECONDS)
                    .build();
        }

        return nativeClient;
    }
}

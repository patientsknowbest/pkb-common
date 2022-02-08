package com.pkb.common.config;

import java.net.URL;

public interface BaseConfig {

    ConfigStorage getConfigStorage();

    default String getBaseURL() {

        // parse it so that we can clean it (don't show port if default, etc.)
        URL baseURL;

        try {
            baseURL = new URL(getConfigStorage().getString("baseURL"));
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid URL value for baseURL: " + getConfigStorage().getString("baseURL"), e);
        }
        int portInt = baseURL.getPort();
        String port = (portInt > 0) ? String.valueOf(portInt) : "";

        if (!baseURL.getPath().isEmpty()) {
            throw new IllegalArgumentException("path not permitted in baseURL: " + baseURL.getPath());
        }

        return buildCleanUrl(
                baseURL.getProtocol(),
                baseURL.getHost(),
                port);
    }

    default boolean isFakeDateTimeServiceEnabled() {
        return getConfigStorage().getBoolean("fakedatetimeservice.enabled", false);
    }

    default String getDefaultDocumentStore() {
        return getConfigStorage().getString("defaultDocumentStore", "DOC");
    }

    default int getGcsInitialTimeout() {
        return getConfigStorage().getInt("gcs.initialTimeout", 1000);
    }

    default int getGcsInitialRpcTimeout() {
        return getConfigStorage().getInt("gcs.initialRpcTimeout", 1000);
    }

    default int getGcsTimeoutMultiplier() {
        return getConfigStorage().getInt("gcs.timeoutMultiplier", 1);
    }

    default int getGcsRpcTimeoutMultiplier() {
        return getConfigStorage().getInt("gcs.rpcTimeoutMultiplier", 1);
    }

    default int getGcsMaxTimeout() {
        return getConfigStorage().getInt("gcs.maxTimeout", 1000);
    }

    default int getGcsMaxRpcTimeout() {
        return getConfigStorage().getInt("gcs.maxRpcTimeout", 1000);
    }

    default int getGcsMaxTotalTimeout() {
        return getConfigStorage().getInt("gcs.maxTotalTimeout", 10000);
    }

    default int getGcsMaxAttempts() {
        return getConfigStorage().getInt("gcs.maxAttempts", 5);
    }

    default String getPubSubProjectName() {
        return getConfigStorage().getString("pubsub.projectname");
    }

    default String getPubSubEmulatorEndpoint() {
        return getConfigStorage().getString("pubsub.emulatorendpoint");
    }

    default String getEnvironmentName() {
        return getConfigStorage().getString("environment.name", "NOT_SET");
    }

    /**
     * Should this application listen for test control requests?
     */
    default boolean isTestControlEnabled() {
        return getConfigStorage().getBoolean("testcontrol.enabled", false);
    }

    /**
     * Should this application register it's test-control endpoint with service registry?
     */
    default boolean isTestControlRegistrationEnabled() {
        return getConfigStorage().getBoolean("testcontrol.registration.enabled", true);
    }

    /**
     * URL for the test-control server where this application will register itself
     */
    default String getTestControlUrl() {
        return getConfigStorage().getString("testcontrol.url");
    }

    /**
     * Unique application name to register on test-control server
     */
    default String getTestControlAppName() {
        return getConfigStorage().getString("testcontrol.appname");
    }

    /**
     * Address for this application's test control endpoint.
     */
    default String getTestControlCallbackUrl() {
        return getConfigStorage().getString("testcontrol.callbackurl", "");
    }

    /**
     * @param protocol defaults to http
     * @param host     valid value required
     * @param port     defaults to none specified (defaults ports 80/443 if specified will be removed)
     * @return example: protocol://host:port/application, or protocol://host no trailing slash unless included in application parameter
     */
    private String buildCleanUrl(String protocol, String host, String port) {
        if ((protocol == null) || protocol.isEmpty()) {
            protocol = "http";
        }
        protocol = protocol.toLowerCase();

        if ((host == null) || host.isEmpty()) {
            throw new IllegalArgumentException("valid host is required");
        }

        String colonPort = ":" + port;
        if ((port == null) || port.isEmpty()) {
            colonPort = "";
        } else if (port.equals("80") || port.equals("443")) {
            colonPort = "";
        }

        if ((port.equals("80") && protocol.equals("https")) ||
                (port.equals("443") && protocol.equals("http"))) {
            throw new IllegalArgumentException("incompatible: protocol " + protocol + " and port " + port);
        }
        return protocol + "://" + host + colonPort;
    }
}

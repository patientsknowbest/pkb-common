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

    default String getPulsarServiceURL() {
        return getConfigStorage().getString("pulsarServiceURL", "pulsar://pulsar:6650");
    }

    default String getPulsarDefaultNamespce() {
        return getConfigStorage().getString("pulsarDefaultNamespce", "defaultNS");
    }

    default boolean isPulsarServiceRegistrationEnabled() {
        return getConfigStorage().getBoolean("pulsarServiceRegistrationEnabled", false);
    }

    default boolean isPulsarTestSupportServicesEnabled() {
        return getConfigStorage().getBoolean("pulsarTestSupportServicesEnabled", false);
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

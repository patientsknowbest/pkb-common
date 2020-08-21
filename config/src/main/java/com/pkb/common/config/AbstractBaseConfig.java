package com.pkb.common.config;

import java.net.URL;

public abstract class AbstractBaseConfig implements BaseConfig {

    final protected ConfigStorage storage;

    AbstractBaseConfig(ConfigStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getBaseURL() {

        // parse it so that we can clean it (don't show port if default, etc.)
        URL baseURL;

        try {
            baseURL = new URL(storage.getString("baseURL"));
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid URL value for baseURL: " + storage.getString("baseURL"), e);
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

    @Override
    public boolean isFakeDateTimeServiceEnabled() {
        return storage.getBoolean("fakedatetimeservice.enabled", false);
    }

    @Override
    public String getPulsarServiceURL() {
        return storage.getString("pulsarServiceURL", "pulsar://pulsar:6650");
    }

    @Override
    public String getPulsarDefaultNamespce() {
        return storage.getString("pulsarDefaultNamespce", "defaultNS");
    }

    @Override
    public boolean isPulsarServiceRegistrationEnabled() {
        return storage.getBoolean("pulsarServiceRegistrationEnabled", false);
    }

    @Override
    public boolean isPulsarTestSupportServicesEnabled() {
        return storage.getBoolean("pulsarTestSupportServicesEnabled", false);
    }
}

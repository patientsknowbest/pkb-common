package com.pkb.common.pubsub.config;

public class GeneralPubsubConfig implements IGeneralPubsubConfig {

    private final String project;
    private final String applicationName;

    public GeneralPubsubConfig(String project, String applicationName) {
        this.project = project;
        this.applicationName = applicationName;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }
}

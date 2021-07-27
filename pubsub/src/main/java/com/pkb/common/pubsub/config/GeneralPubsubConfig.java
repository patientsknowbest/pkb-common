package com.pkb.common.pubsub.config;

public class GeneralPubsubConfig implements IGeneralPubsubConfig {

    private final String project;
    private final String serviceAccountKey;

    public GeneralPubsubConfig(String project, String serviceAccountKey) {
        this.project = project;
        this.serviceAccountKey = serviceAccountKey;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public String getServiceAccountKey() {
        return serviceAccountKey;
    }


}

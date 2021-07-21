package com.pkb.common.pubsub.config;

public class GeneralPubsubConfig implements IGeneralPubsubConfig {

    private final String project;

    public GeneralPubsubConfig(String project) {
        this.project = project;
    }

    @Override
    public String getProject() {
        return project;
    }

}

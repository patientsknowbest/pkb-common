package com.pkb.common.testsupport.services;

public class DefaultPubSubNamespaceService implements PubSubNamespaceService {

    private String currentNamespace = "defaultNS";

    @Override
    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    @Override
    public String getCurrentNamespace() {
        return currentNamespace;
    }
}

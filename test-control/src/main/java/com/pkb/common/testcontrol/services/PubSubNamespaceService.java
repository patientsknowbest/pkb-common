package com.pkb.common.testcontrol.services;

public interface PubSubNamespaceService {

    void setCurrentNamespace(String namespace);

    String getCurrentNamespace();
}

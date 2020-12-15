package com.pkb.common.testsupport.services;

public interface PubSubNamespaceService {

    void setCurrentNamespace(String namespace);

    String getCurrentNamespace();
}

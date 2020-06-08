package com.pkb.common;

import javax.inject.Inject;

import com.pkb.common.config.ConfigStorage;

public class InjectableSecondaryConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    private static final long serialVersionUID = -48241001152877711L;

    @Inject
    public InjectableSecondaryConfigTestSupportServlet(ConfigStorage configStorage) {
        this.configStorage = configStorage;
    }
}

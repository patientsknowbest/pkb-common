package com.pkb.common;

import com.pkb.common.config.PrimaryBaseConfig;

import javax.inject.Inject;

public class InjectablePrimaryConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    @Inject
    public InjectablePrimaryConfigTestSupportServlet(PrimaryBaseConfig primaryBaseConfig) {
        this.baseConfig = primaryBaseConfig;
    }
}

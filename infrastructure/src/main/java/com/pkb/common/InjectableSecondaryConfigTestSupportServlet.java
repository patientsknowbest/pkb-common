package com.pkb.common;

import com.pkb.common.config.SecondaryBaseConfig;

import javax.inject.Inject;

public class InjectableSecondaryConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    @Inject
    public InjectableSecondaryConfigTestSupportServlet(SecondaryBaseConfig seondaryBaseConfig) {
        this.baseConfig = seondaryBaseConfig;
    }
}

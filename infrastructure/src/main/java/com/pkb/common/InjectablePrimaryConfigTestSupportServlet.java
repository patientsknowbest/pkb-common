package com.pkb.common;

import javax.inject.Inject;

import com.pkb.common.config.PrimaryBaseConfig;

public class InjectablePrimaryConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    private static final long serialVersionUID = 9059641924827182771L;

    @Inject
    public InjectablePrimaryConfigTestSupportServlet(PrimaryBaseConfig primaryBaseConfig) {
        this.baseConfig = primaryBaseConfig;
    }
}

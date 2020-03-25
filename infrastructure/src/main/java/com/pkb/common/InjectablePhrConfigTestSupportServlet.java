package com.pkb.common;

import com.pkb.common.config.PkbPluginConfig;

import javax.inject.Inject;

public class InjectablePhrConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    @Inject
    public InjectablePhrConfigTestSupportServlet(PkbPluginConfig pkbPluginConfig) {
        this.baseConfig = pkbPluginConfig;
    }
}

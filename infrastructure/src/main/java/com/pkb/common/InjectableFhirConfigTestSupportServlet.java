package com.pkb.common;

import com.pkb.common.config.FhirConfig;

import javax.inject.Inject;

public class InjectableFhirConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    @Inject
    public InjectableFhirConfigTestSupportServlet(FhirConfig configV2) {
        this.configV2 = configV2;
    }
}

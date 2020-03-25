package com.pkb.common;

import com.pkb.common.config.FhirConfig;

import javax.inject.Inject;

public class InjectableFhirConfigTestSupportServlet extends InjectableConfigTestSupportServlet {

    @Inject
    public InjectableFhirConfigTestSupportServlet(FhirConfig fhirConfig) {
        this.baseConfig = fhirConfig;
    }
}

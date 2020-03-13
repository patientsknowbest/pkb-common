package com.pkb.common;

import com.pkb.common.config.ConfigV2;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectableConfigTestSupportServlet extends HttpServlet {

    private ConfigV2 configV2;

    @Inject
    public InjectableConfigTestSupportServlet(ConfigV2 configV2) {
        this.configV2 = configV2;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (configV2.isMutableConfigEnabled()) {
                String key = req.getParameter("key");
                String value = req.getParameter("value");
                configV2.setValue(key, value);
            }
        } catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (configV2.isMutableConfigEnabled()) {
            configV2.reset();
        }
    }
}

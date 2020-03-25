package com.pkb.common;

import com.pkb.common.config.BaseConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectableConfigTestSupportServlet extends HttpServlet {

    protected BaseConfig baseConfig;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (baseConfig.isMutableConfigEnabled()) {
                String key = req.getParameter("key");
                String value = req.getParameter("value");
                baseConfig.setValue(key, value);
            }
        } catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (baseConfig.isMutableConfigEnabled()) {
            baseConfig.reset();
        }
    }
}

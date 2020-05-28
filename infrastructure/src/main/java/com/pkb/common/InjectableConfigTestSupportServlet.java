package com.pkb.common;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pkb.common.config.BaseConfig;

public class InjectableConfigTestSupportServlet extends HttpServlet {

    private static final long serialVersionUID = 2511069551123167217L;
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

package com.pkb.common;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pkb.common.datetime.DateTimeService;

public class DateTimeTestSupportServlet extends HttpServlet {

    private static final long serialVersionUID = -8888887639985106107L;
    private DateTimeService dateTimeService;

    @Inject
    public DateTimeTestSupportServlet(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println(dateTimeService.now());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String dateTime = req.getParameter("dateTime");
            dateTimeService.setFixedCurrentTimeForTesting(dateTime);
        } catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        dateTimeService.forgetFixedCurrentTimeForTesting();
    }
}

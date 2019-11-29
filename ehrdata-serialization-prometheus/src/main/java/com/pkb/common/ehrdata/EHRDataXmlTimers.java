package com.pkb.common.ehrdata;

import java.io.IOException;

import io.prometheus.client.Summary;

public class EHRDataXmlTimers {

        private static final Summary XML_PARSE_SECONDS = Summary.build()
                .name("pkb_phr_menudata_xmlparseseconds")
                .help("Time needed to parse xml01")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .quantile(0.95, 0.005)
                .quantile(0.99, 0.001)
                .register();

        private static final Summary XML_CREATE_SECONDS = Summary.build()
                .name("pkb_phr_menudata_xmlcreateseconds")
                .help("Time needed to serialize fields to xml01")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .quantile(0.95, 0.005)
                .quantile(0.99, 0.001)
                .register();

        public static final XmlTimerPrometheus AUTOCLOSEABLE_XML_PARSE_SECONDS = new XmlTimerPrometheus(XML_PARSE_SECONDS);
        public static final XmlTimerPrometheus AUTOCLOSEABLE_XML_CREATE_SECONDS = new XmlTimerPrometheus(XML_CREATE_SECONDS);

        static class XmlTimerPrometheus implements AutocloseableTimer
        {
                Summary summary;
                Summary.Timer timer;

                public XmlTimerPrometheus(Summary summary)
                {
                        this.summary = summary;
                }

                @Override
                public AutocloseableTimer startTimer() {
                        timer = summary.startTimer();
                        return this;
                }

                @Override
                public double observeDuration() {
                        return timer.observeDuration();
                }

                @Override
                public void close() throws IOException {
                        observeDuration();
                }
        }
}

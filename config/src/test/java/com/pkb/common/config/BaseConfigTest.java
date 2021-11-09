package com.pkb.common.config;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThrows;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameBeanAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@SuppressWarnings("ThrowableNotThrown")
class BaseConfigTest {

    private DummyConfig underTest = new DummyConfig(createStorage());

    private class DummyConfig implements BaseConfig {

        private final ConfigStorage storage;

        DummyConfig(ConfigStorage storage) {
            this.storage = storage;
        }

        @Override
        public ConfigStorage getConfigStorage() {
            return storage;
        }
    }

    @Test
    void testGetBaseUrlValid() {
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test
    void testGetBaseUrlInvalid() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "fubar");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        //WHEN - THEN
        var cause = new MalformedURLException("no protocol: fubar");
        var expected = new IllegalArgumentException("invalid URL value for baseURL: fubar");
        expected.initCause(cause);
        assertThrows(sameBeanAs(expected), () -> underTest.getBaseURL());
    }

    @Test
    void testGetBaseUrlNull() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", null);
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        //WHEN - THEN
        var cause = new MalformedURLException();
        var cause2 = new NullPointerException();
        cause.initCause(cause2);
        var expected = new IllegalArgumentException("invalid URL value for baseURL: null");
        expected.initCause(cause);
        assertThrows(sameBeanAs(expected), () -> underTest.getBaseURL());
    }

    @Test
    void testGetBaseUrlSucceedsWithoutPort() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test
    void testGetBaseUrlFailsWithPath() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost/fubar");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        //WHEN - THEN
        var expected = new IllegalArgumentException("path not permitted in baseURL: /fubar");
        assertThrows(sameBeanAs(expected), () -> underTest.getBaseURL());
    }

    @Test
    void testGetBaseUrlFailsWithMismatchedProtocolHttp() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost:443");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        //WHEN - THEN
        var expected = new IllegalArgumentException("incompatible: protocol http and port 443");
        assertThrows(sameBeanAs(expected), () -> underTest.getBaseURL());
    }

    @Test
    void testGetBaseUrlFailsWithMismatchedProtocolHttps() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "https://localhost:80");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        //WHEN - THEN
        var expected = new IllegalArgumentException("incompatible: protocol https and port 80");
        assertThrows(sameBeanAs(expected), () -> underTest.getBaseURL());
    }

    @Test
    void testGetBaseUrlFailsWhenHostIsMissing() {
        //GIVEN
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://:80");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        //WHEN - THEN
        var expected = new IllegalArgumentException("valid host is required");
        assertThrows(sameBeanAs(expected), () -> underTest.getBaseURL());
    }

    @Test
    void testFakeDateTimeServiceDisabledByDefault() {
        underTest = new DummyConfig(new ImmutableRawConfigStorage(new HashMap<>()));
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    @Test
    void testFakeDateTimeServiceUsesConfigValue() {
        Map<String, String> map = new HashMap<>();
        map.put("fakedatetimeservice.enabled", "true");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(true));
    }

    private ImmutableRawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost:80");
        map.put("fakedatetimeservice.enabled", "false");
        return new ImmutableRawConfigStorage(map);
    }
}

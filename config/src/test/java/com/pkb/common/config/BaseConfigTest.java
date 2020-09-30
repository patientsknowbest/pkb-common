package com.pkb.common.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class BaseConfigTest {

    private DummyConfig underTest = new DummyConfig(createStorage());

    private class DummyConfig implements BaseConfig{

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
    public void testGetBaseUrlValid() {
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlInvalid() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "fubar");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        // throws expected exception
        underTest.getBaseURL();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlNull() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", null);
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        // throws expected exception
        underTest.getBaseURL();
    }

    @Test
    public void testGetBaseUrlSucceedsWithoutPort() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlFailsWithPath() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost/fubar");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlFailsWithMismatchedProtocolHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost:443");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlFailsWithMismatchedProtocolHttps() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "https://localhost:80");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlFailsWhenHostIsMissing() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://:80");
        underTest = new DummyConfig(new ImmutableRawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test
    public void testFakeDateTimeServiceDisabledByDefault() {
        underTest = new DummyConfig(new ImmutableRawConfigStorage(new HashMap<>()));
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    @Test
    public void testFakeDateTimeServiceUsesConfigValue() {
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

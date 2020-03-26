package com.pkb.common.config;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BaseImmutableConfigTest {

    private BaseImmutableConfig underTest;

    @Before
    public void before() {
        underTest = new BaseImmutableConfig(createStorage());
    }

    @Test
    public void testImmutableByDefault() {
        assertThat(underTest.isMutableConfigEnabled(), equalTo(false));
    }

    @Test
    public void testCannotBeMadeMutable() {
        underTest.setValue("mutableConfig.enabled", "true");
        assertThat(underTest.isMutableConfigEnabled(), equalTo(false));
    }

    @Test
    public void testGetBaseUrlValid() {
        underTest.setValue("mutableConfig.enabled", "true");
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlInvalid() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "fubar");
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        // throws expected exception
        underTest.getBaseURL();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBaseUrlNull() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", null);
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        // throws expected exception
        underTest.getBaseURL();
    }

    @Test
    public void testGetBaseUrlSucceedsWithoutPort() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost");
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetBaseUrlFailsWithPath() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost/fubar");
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetBaseUrlFailsWithMismatchedProtocolHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost:443");
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetBaseUrlFailsWithMismatchedProtocolHttps() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "https://localhost:80");
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetBaseUrlFailsWhenHostIsMissing() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://:80");
        underTest = new BaseImmutableConfig( new RawConfigStorage(map));
        // Expect exception
        underTest.getBaseURL();
    }

    @Test
    public void testFakeDateTimeServiceDisabledByDefault() {
        underTest = new BaseImmutableConfig( new RawConfigStorage(new HashMap<>()));
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    @Test
    public void testFakeDateTimeServiceUsesConfigValue() {
        Map<String, String> map = new HashMap<>();
        map.put("fakedatetimeservice.enabled", "true");
        underTest = new BaseImmutableConfig(new RawConfigStorage(map));
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(true));
    }

    @Test
    public void testFakeDateTimeServiceDisabledCannotBeMutated() {
        underTest = new BaseImmutableConfig( new RawConfigStorage(new HashMap<>()));
        underTest.setValue("fakeDateTimeServiceEnabled", "true");
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    private RawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost:80");
        map.put("fakedatetimeservice.enabled", "false");
        return new RawConfigStorage(map);
    }
}

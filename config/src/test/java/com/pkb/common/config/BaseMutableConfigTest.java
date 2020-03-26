package com.pkb.common.config;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BaseMutableConfigTest {
    BaseMutableConfig underTest;

    @Before
    public void before() {
        underTest = new BaseMutableConfig() {
            @Override
            BaseConfig getDefaultConfig() {
                return new BaseImmutableConfig(createStorage());
            }
        };
    }

    @Test
    public void testGetBaseUrlFallsBackToDefaultWhenNotOverridden() {
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test
    public void testGetBaseUrlReturnsOverride() {
        underTest.setValue("baseURL", "something");
        assertThat(underTest.getBaseURL(), equalTo("something"));
    }

    @Test
    public void testGetBaseUrlCanBeOverriddenAndThenResetToDefault() {
        underTest.setValue("baseURL", "something");
        assertThat(underTest.getBaseURL(), equalTo("something"));
        underTest.reset();
        assertThat(underTest.getBaseURL(), equalTo("http://localhost"));
    }

    @Test
    public void testFakeDateTimeFallsBackToDefaultWhenNotOverridden() {
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    @Test
    public void testFakeDateTimeReturnsOverride() {
        underTest.setValue("fakeDateTimeServiceEnabled", "true");
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(true));
    }

    @Test
    public void testFakeDateTimeReturnsDefaultWhenOverriddenWithInvalidValue() {
        underTest.setValue("fakeDateTimeServiceEnabled", "fubar");
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    @Test
    public void testFakeDateTimeCanBeOverriddenAndThenResetToDefault() {
        underTest.setValue("fakeDateTimeServiceEnabled", "true");
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(true));
        underTest.reset();
        assertThat(underTest.isFakeDateTimeServiceEnabled(), equalTo(false));
    }

    private RawConfigStorage createStorage() {
        Map<String, String> map = new HashMap<>();
        map.put("baseURL", "http://localhost:80");
        map.put("fakedatetimeservice.enabled", "false");
        return new RawConfigStorage(map);
    }
}

package com.pkb.common.config;

import static com.pkb.common.config.LayeredLoader.DEFAULT_CONFIG_FILE_PATH;
import static com.pkb.common.config.LayeredLoader.mapEnvVarValueToPropertyFileList;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class LayeredLoaderTest {

    private static final String DEFAULT_CONFIG_FILE_NAME =DEFAULT_CONFIG_FILE_PATH + ".properties";

    @Test
    public void mapEnvVarValueToPropertyFileList_nullValue() {
        assertEquals(singletonList(DEFAULT_CONFIG_FILE_NAME), mapEnvVarValueToPropertyFileList(null));
    }

    @Test
    public void mapEnvVarValueToPropertyFileList_onlyDefaultIsPassed() {
        assertEquals(singletonList(DEFAULT_CONFIG_FILE_NAME), mapEnvVarValueToPropertyFileList(DEFAULT_CONFIG_FILE_PATH));
    }

    @Test
    public void mapEnvVarValueToPropertyFileList_defaultIsFirst() {
        List<String> expected = asList(DEFAULT_CONFIG_FILE_NAME, "config/dev.properties");
        assertEquals(expected, mapEnvVarValueToPropertyFileList(DEFAULT_CONFIG_FILE_PATH + ":config/dev"));
    }

    @Test
    public void mapEnvVarValueToPropertyFileList_defaultIsNotFirst() {
        assertEquals(asList(DEFAULT_CONFIG_FILE_NAME, "config/dev.properties"), mapEnvVarValueToPropertyFileList("config/dev"));
    }
}

package com.pkb.common.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigLoaderTest {

    @Test
    public void testReadConfiguration() throws Exception {
        RawConfigStorage baseConfiguration = new PropertyFileBasedLoader("base_config.properties").load();
        assertEquals("str1", baseConfiguration.getString("strProp1"));
        assertEquals("str2", baseConfiguration.getString("strProp2"));
        assertEquals(1, baseConfiguration.getInt("numProp1"));
    }

    @Test
    public void testMergeConfiguration() throws Exception {
        RawConfigStorage merged = LayeredLoader.createLoaderForEnvVar("base_config:layer1_config").load();
        assertEquals("str1", merged.getString("strProp1"));
        assertEquals("str22", merged.getString("strProp2"));
        assertEquals("str32", merged.getString("strProp3"));
        assertEquals(1, merged.getInt("numProp1"));
        assertEquals(1, merged.getInt("numProp1"));
    }

    @Test
    public void testEmptyProperty() throws Exception {
        assertEquals("", new PropertyFileBasedLoader("base_config.properties").load().getString("prop3"));
    }


}
